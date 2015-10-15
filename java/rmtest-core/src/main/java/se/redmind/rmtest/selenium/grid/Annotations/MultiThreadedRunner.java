package se.redmind.rmtest.selenium.grid.Annotations;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;

public class MultiThreadedRunner extends BlockJUnit4ClassRunner {

    public MultiThreadedRunner(final Class<?> klass) throws InitializationError {
        super(klass);
        int threadCount = klass.isAnnotationPresent(MultiThreadedSuite.Concurrent.class)
            ? klass.getAnnotation(MultiThreadedSuite.Concurrent.class).threads()
            : (int) (Runtime.getRuntime().availableProcessors() * 1.5);
        setScheduler(new RunnerScheduler() {
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount, new MultiThreadFactory(klass.getSimpleName()));
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
