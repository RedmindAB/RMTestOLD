package cucumber.runtime;

import java.util.LinkedList;

public interface StopWatch {

    void start();

    /**
     * @return nanoseconds since start
     */
    long stop();

    StopWatch SYSTEM = new StopWatch() {
        private final ThreadLocal<LinkedList<Long>> start = new ThreadLocal<LinkedList<Long>>() {
            @Override
            protected LinkedList<Long> initialValue() {
                return new LinkedList<>();
            }
        };

        @Override
        public void start() {
            start.get().add(System.nanoTime());
        }

        @Override
        public long stop() {
            Long duration = System.nanoTime() - start.get().removeLast();
            return duration;
        }
    };

    public static class Stub implements StopWatch {

        private final long duration;

        public Stub(long duration) {
            this.duration = duration;
        }

        @Override
        public void start() {
        }

        @Override
        public long stop() {
            return duration;
        }
    }
}
