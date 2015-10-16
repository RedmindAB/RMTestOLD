package se.redmind.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * This class is just a repeater.
 *
 * The current implementation is not thread safe as it is not immutable.
 *
 * usage example:
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
 * </pre>
 *
 * @author Jeremy Comte
 */
public final class Try {

    public static <E> SupplierTryer<E> toGet(Supplier<E> supplier) {
        return new SupplierTryer<>(supplier);
    }

    public static RunnableTryer toExecute(Runnable runnable) {
        return new RunnableTryer(runnable);
    }

    public abstract static class Tryer<E, SelfType> {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());
        private final Supplier<E> supplier;
        private int currentAttempt;
        private int maxAttempts = 1;
        private TimeUnit sleepUnit;
        private long sleepLength;
        private BiConsumer<Tryer<E, SelfType>, Exception> onError;
        private BiConsumer<Tryer<E, SelfType>, Exception> onLastError;
        protected Supplier<E> defaultSupplier;
        protected Predicate<E> until;

        protected Tryer(Supplier<E> supplier) {
            this.supplier = supplier;
        }

        /**
         * set a specific function to be used when an error is encountered, default will printout a warning
         *
         * @param onError
         * @return
         */
        public SelfType onError(BiConsumer<Tryer<E, SelfType>, Exception> onError) {
            this.onError = onError;
            return (SelfType) this;
        }

        /**
         * set a specific function to be used if all the attempts are unsuccessful, default will printout an error
         *
         * @param onError
         * @return
         */
        public SelfType onLastError(BiConsumer<Tryer<E, SelfType>, Exception> onLastError) {
            this.onLastError = onLastError;
            return (SelfType) this;
        }

        /**
         * when an error is encountered, delay the next attempt by this delay in milliseconds
         *
         * @param sleepLengthInMillisecs
         * @return
         */
        public SelfType delayRetriesBy(long sleepLengthInMillisecs) {
            return delayRetriesBy(sleepLengthInMillisecs, TimeUnit.MILLISECONDS);
        }

        /**
         * when an error is encountered, delay the next attempt by this delay
         *
         * @param sleepLength
         * @param sleepUnit
         * @return
         */
        public SelfType delayRetriesBy(long sleepLength, TimeUnit sleepUnit) {
            this.sleepLength = sleepLength;
            this.sleepUnit = sleepUnit;
            return (SelfType) this;
        }

        /**
         * Try just once to run this code
         *
         * @return
         * @throws InterruptedException
         */
        public E justOnce() throws InterruptedException {
            return execute();
        }

        /**
         * Try n times to run this code
         *
         * @param maxAttempts
         * @return
         */
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
                    } else if (maxAttempts > 1) {
                        logger.warn(exception.toString() + " on attempt " + currentAttempt + "/" + maxAttempts);
                    }
                    if (currentAttempt == maxAttempts) {
                        if (onLastError != null) {
                            onLastError.accept(this, exception);
                        } else {
                            logger.error(exception.toString(), exception);
                        }
                    }
                }
                if (currentAttempt < maxAttempts && sleepLength > 0) {
                    try {
                        sleepUnit.sleep(sleepLength);
                    } catch (InterruptedException ex) {
                        logger.error(ex.toString(), ex);
                    }
                } else if (defaultSupplier != null) {
                    result = defaultSupplier.get();
                }
            }

            if (until != null && !until.test(result) && defaultSupplier == null) {
                throw new IllegalStateException("Couldn't get a valid value and no default was supplied...");
            }
            return result;
        }
    }

    public static class SupplierTryer<E> extends Tryer<E, SupplierTryer<E>> {

        public SupplierTryer(Supplier<E> supplier) {
            super(supplier);
        }

        /**
         * Retry until this predicate is true and that maxAttempts hasn't been reached
         *
         * @param predicate
         * @return
         */
        public synchronized SupplierTryer<E> until(Predicate<E> predicate) {
            this.until = predicate;
            return this;
        }

        /**
         * if all the attempts have been unsuccessful, default to this value
         *
         * @param defaultSupplier
         * @return
         */
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
