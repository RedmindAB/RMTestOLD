package se.redmind.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * this class is just a repeater.
 *
 * One could use it in that way:
 *
 *       return Try.toGet(() -> driver.getTitle().contains(articleId))
 *                  .defaultTo(() -> false)
 *                  .delayRetriesBy(50, TimeUnit.MILLISECONDS)
 *                  .nTimes(10);
 *
 * instead of:
 *       for (int i = 0; i < 10; i++) {
 *           try {
 *               return driver.getTitle().contains(articleId);
 *           } catch (Exception e) {
 *               TimeUnit.MILLISECONDS.sleep(50);
 *           }
 *       }
 *       return false;
 * </pre> @author Jeremy Comte
 */
public final class Try {

    public static <E> SupplierTryer<E> toGet(Supplier<E> supplier) {
        return new SupplierTryer<>(supplier);
    }

    public static RunnableTryer toExecute(Runnable runnable) {
        return new RunnableTryer(runnable);
    }

    public static class Tryer<E, SelfType> {

        protected final Logger logger = LoggerFactory.getLogger(this.getClass());
        protected final Supplier<E> supplier;
        protected Supplier<E> defaultSupplier;
        protected Predicate<E> until;
        protected int currentAttempt;
        protected int maxAttempts = 1;
        protected TimeUnit sleepUnit;
        protected long sleepLength;
        protected BiConsumer<Tryer<E, SelfType>, Exception> onError;
        protected BiConsumer<Tryer<E, SelfType>, Exception> onLastError;

        public Tryer(Supplier<E> supplier) {
            this.supplier = supplier;
        }

        public int maxAttempts() {
            return maxAttempts;
        }

        public int currentAttempt() {
            return currentAttempt;
        }

        public SelfType onError(BiConsumer<Tryer<E, SelfType>, Exception> onError) {
            this.onError = onError;
            return (SelfType) this;
        }

        public SelfType onLastError(BiConsumer<Tryer<E, SelfType>, Exception> onLastError) {
            this.onLastError = onLastError;
            return (SelfType) this;
        }

        public SelfType delayRetriesBy(long sleepLengthInMillisecs) {
            return delayRetriesBy(sleepLengthInMillisecs, TimeUnit.MILLISECONDS);
        }

        public SelfType delayRetriesBy(long sleepLength, TimeUnit sleepUnit) {
            this.sleepLength = sleepLength;
            this.sleepUnit = sleepUnit;
            return (SelfType) this;
        }

        public E justOnce() throws InterruptedException {
            return execute();
        }

        public synchronized E nTimes(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return execute();
        }

        private synchronized E execute() {
            E result = null;
            for (currentAttempt = 1; currentAttempt <= maxAttempts; currentAttempt++) {
                try {
                    result = supplier.get();
                    if (until == null || until.test(result)) {
                        break;
                    }
                } catch (Exception exception) {
                    if (onError != null) {
                        onError.accept(this, exception);
                    } else if(maxAttempts > 1){
                        logger.warn(exception.toString() + " on attempt " + currentAttempt + "/" + maxAttempts);
                    }
                    if (currentAttempt == maxAttempts) {
                        if (onLastError != null) {
                            onLastError.accept(this, exception);
                        } else {
                            logger.error(exception.toString(), exception);
                        }
                    } else if (sleepLength > 0) {
                        try {
                            sleepUnit.sleep(sleepLength);
                        } catch (InterruptedException ex) {
                            logger.error(ex.toString(), ex);
                        }
                    }
                    if (currentAttempt == maxAttempts && defaultSupplier != null) {
                        result = defaultSupplier.get();
                    }
                }
            }
            if (until != null && !until.test(result) && defaultSupplier == null) {
                throw new IllegalStateException("no valid value and no default supplied...");
            }
            return result;
        }
    }

    public static class SupplierTryer<E> extends Tryer<E, SupplierTryer<E>> {

        public SupplierTryer(Supplier<E> supplier) {
            super(supplier);
        }

        public synchronized SupplierTryer<E> until(Predicate<E> predicate) {
            this.until = predicate;
            return this;
        }

        public SupplierTryer<E> defaultTo(Supplier<E> defaultSupplier) {
            this.defaultSupplier = defaultSupplier;
            return this;
        }
    }

    public static class RunnableTryer extends Tryer<Void, RunnableTryer> {

        public RunnableTryer(Runnable runnable) {
            super(() -> {
                runnable.run();
                return null;
            });
        }
    }
}
