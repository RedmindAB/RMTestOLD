package se.redmind.rmtest.selenium.grid.Annotations;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by johgri on 15-09-24.
 */
public class MultiThreadFactory implements ThreadFactory{

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;

    public MultiThreadFactory(String poolName) {
        group = new ThreadGroup(poolName + "-" + POOL_NUMBER.getAndIncrement());
    }

    @Override
    public Thread newThread(Runnable runnable) {
        return new Thread(group, runnable, group.getName() + "-thread-" + threadNumber.getAndIncrement(), 0);
    }
}
