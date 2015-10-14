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

    public static abstract class Tryer<SelfType> {

        protected final Logger logger = LoggerFactory.getLogger(this.getClass());
        protected int currentAttempt;
        protected int maxAttempts = 1;
        protected TimeUnit sleepUnit;
        protected long sleepLength;
        protected BiConsumer<Tryer<SelfType>, Exception> onError;
        protected BiConsumer<Tryer<SelfType>, Exception> onLastError;

        public int maxAttempts() {
            return maxAttempts;
        }

        public int currentAttempt() {
            return currentAttempt;
        }

        public SelfType onError(BiConsumer<Tryer<SelfType>, Exception> onError) {
            this.onError = onError;
            return (SelfType) this;
        }

        public SelfType onLastError(BiConsumer<Tryer<SelfType>, Exception> onLastError) {
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

        protected void handleException(Exception exception) {
            if (onError != null) {
                onError.accept(this, exception);
            } else {
                logger.warn(exception.getMessage() + " on attempt " + currentAttempt + "/" + maxAttempts);
            }
            if (currentAttempt == maxAttempts - 1) {
                if (onLastError != null) {
                    onLastError.accept(this, exception);
                } else {
                    logger.error(exception.getMessage(), exception);
                }
            } else if (sleepLength > 0) {
                try {
                    sleepUnit.sleep(sleepLength);
                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    }

    public static class SupplierTryer<E> extends Tryer<SupplierTryer<E>> {

        private final Supplier<E> supplier;
        private Supplier<E> defaultSupplier;
        private Predicate<E> until;

        public SupplierTryer(Supplier<E> supplier) {
            this.supplier = supplier;
        }

        public synchronized SupplierTryer<E> until(Predicate<E> predicate) {
            this.until = predicate;
            return this;
        }

        public E justOnce() throws InterruptedException {
            return execute();
        }

        public synchronized E nTimes(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return execute();
        }

        public SupplierTryer<E> defaultTo(Supplier<E> defaultSupplier) {
            this.defaultSupplier = defaultSupplier;
            return this;
        }

        private synchronized E execute() {
            E result = null;
            for (currentAttempt = 0; currentAttempt < maxAttempts; currentAttempt++) {
                try {
                    result = supplier.get();
                    if (until == null || until.test(result)) {
                        break;
                    }
                } catch (Exception e) {
                    handleException(e);
                    if (currentAttempt == maxAttempts - 1 && defaultSupplier != null) {
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

    public static class RunnableTryer extends Tryer<RunnableTryer> {

        private final Runnable runnable;

        public RunnableTryer(Runnable runnable) {
            this.runnable = runnable;
        }

        public void justOnce() {
            nTimes(1);
        }

        public void nTimes(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            execute();
        }

        private synchronized void execute() {
            for (currentAttempt = 0; currentAttempt < maxAttempts; currentAttempt++) {
                try {
                    runnable.run();
                    break;
                } catch (Exception e) {
                    handleException(e);
                }
            }
        }
    }
}