package se.redmind.rmtest.selenium.grid.Annotations;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by johgri on 15-09-24.
 */
public class MultiThreadFactory implements ThreadFactory{

    static final AtomicInteger poolNumber = new AtomicInteger(1);
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final ThreadGroup group;

    MultiThreadFactory(String poolName) {
        group = new ThreadGroup(poolName + "-" + poolNumber.getAndIncrement());
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(group, runnable, group.getName() + "-thread-" + threadNumber.getAndIncrement(), 0);
    }
}
