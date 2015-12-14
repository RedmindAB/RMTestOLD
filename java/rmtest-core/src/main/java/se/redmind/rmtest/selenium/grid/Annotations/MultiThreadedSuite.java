package se.redmind.rmtest.selenium.grid.Annotations;

/**
 * Created by johgri on 15-09-23.
 */
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;

import se.redmind.rmtest.runners.Parallelize;

public class MultiThreadedSuite extends Suite {

    public MultiThreadedSuite(final Class<?> klass) throws InitializationError {
        super(klass, new AllDefaultPossibilitiesBuilder(true) {
            @Override
            public Runner runnerForClass(Class<?> testClass) throws Throwable {
                List<RunnerBuilder> builders = Arrays.asList(
                    new RunnerBuilder() {
                        @Override
                        public Runner runnerForClass(Class<?> testClass) throws Throwable {
                            if (testClass.isAnnotationPresent(Parallelize.class)) {
                                return new MultiThreadedRunner(testClass);
                            }
                            return null;
                        }
                    },
                    ignoredBuilder(),
                    annotatedBuilder(),
                    suiteMethodBuilder(),
                    junit3Builder(),
                    junit4Builder());
                for (RunnerBuilder each : builders) {
                    Runner runner = each.safeRunnerForClass(testClass);
                    if (runner != null) {
                        return runner;
                    }
                }
                return null;
            }
        });
        setScheduler(new RunnerScheduler() {
            ExecutorService executorService = Executors.newFixedThreadPool(
                klass.isAnnotationPresent(Parallelize.class)
                    ? klass.getAnnotation(Parallelize.class).threads()
                    : (int) (Runtime.getRuntime().availableProcessors() * 1.5),
                new MultiThreadFactory(klass.getSimpleName()));
            CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);
            Queue<Future<Void>> tasks = new LinkedList<>();

            @Override
            public void schedule(Runnable childStatement) {
                tasks.offer(completionService.submit(childStatement, null));
            }

            @Override
            public void finished() {
                try {
                    while (!tasks.isEmpty()) {
                        tasks.remove(completionService.take());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    while (!tasks.isEmpty()) {
                        tasks.poll().cancel(true);
                    }
                    executorService.shutdownNow();
                }
            }
        });
    }

}
