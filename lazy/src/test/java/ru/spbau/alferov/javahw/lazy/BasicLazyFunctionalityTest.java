package ru.spbau.alferov.javahw.lazy;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>This is the class for basic functionality tests.</p>
 *
 * <p>In all of this functions Lazy is taken from some function which takes Supplier&lt;T&gt;.</p>
 *
 * <p>This class is used to test at least both {@link LazyFactory#createSingleThreadLazy(Supplier)} and
 * {@link LazyFactory#createMultiThreadLazy(Supplier)}.</p>
 */
public class BasicLazyFunctionalityTest {
    /**
     * Checks that returned Lazy object returns the proper value at the first and the second call.
     *
     * @param lazyTransformer The transformer to be tested (see {@link BasicLazyFunctionalityTest class documentation}
     *                        for information)
     */
    public static void lazyReturnValueTest(Function<Supplier<Integer>, Lazy<Integer>> lazyTransformer) {
        Lazy<Integer> lazy = lazyTransformer.apply(() -> 179);
        assertEquals(new Integer(179), lazy.get());
        assertEquals(new Integer(179), lazy.get());
    }

    /**
     * Checks that the Lazy object created from returning null supplier returns the proper value at the first and the
     * second call.
     *
     * @param lazyTransformer The transformer to be tested (see {@link BasicLazyFunctionalityTest class documentation}
     *                        for information).
     */
    public static void lazyReturnNullTest(Function<Supplier<Object>, Lazy<Object>> lazyTransformer) {
        Lazy<Object> lazy = lazyTransformer.apply(() -> null);
        assertEquals(null, lazy.get());
        assertEquals(null, lazy.get());
    }

    /**
     * <p>This class provides a constant-returning Supplier&lt;T&gt; counting number of calls to
     * {@link CallCounter#get() get()} function.</p>
     *
     * <p>The returned value can be null.</p>
     *
     * <p>This class is used by {@link MultiThreadLazyTest#hundredThreadsConcurrentTest()} so it is declared public.</p>
     *
     * @param <T> Type of constant to be returned.
     */
    public static class CallCounter<T> implements Supplier<T> {
        /**
         * This is the value to be returned.
         */
        @Nullable private T returnValue;

        /**
         * <p>This is number of calls to {@link CallCounter#get() get()} function.</p>
         */
        private int callCount = 0;

        /**
         * Constructs a new CallCounter returning the given value.
         *
         * @param toReturn The value to be returned. Note that it might be null.
         */
        public CallCounter(@Nullable T toReturn) {
            returnValue = toReturn;
        }

        /**
         * <p>Increments the call counter and returns the stored value.</p>
         *
         * <p>Note that the function is synchronized for multi-thread testing reasons. Even volatile variables cannot be
         * incremented in atomic way so we need this function to be synchronized.</p>
         *
         * @return The value to be returned.
         */
        @Override
        public synchronized T get() {
            callCount++;
            return returnValue;
        }

        /**
         * <p>Returns the number of calls to get function.</p>
         *
         * <p>This function is not synchronized to callCount value and is not supposed to be called in multiple threads
         * simultaneously.</p>
         */
        public int getCallCount() {
            return callCount;
        }

    }

    /**
     * Checks that returned Lazy objects calls the supplier exactly once. The supplier in this test returns a not-null
     * object.
     *
     * @param lazyTransformer The transformer to be tested (see {@link BasicLazyFunctionalityTest class documentation}
     *                        for information).
     */
    public static void lazyCallsOnceTest(Function<Supplier<Integer>, Lazy<Integer>> lazyTransformer) {
        CallCounter<Integer> callCounter = new CallCounter<>(179);
        Lazy<Integer> lazy = lazyTransformer.apply(callCounter);
        assertEquals(new Integer(179), lazy.get());
        assertEquals(new Integer(179), lazy.get());
        assertEquals(1, callCounter.getCallCount());
    }

    /**
     * Checks that returned Lazy object calls the supplier exactly once. The supplier in this test returns null.
     *
     * @param lazyTransformer The transformer to be tested (see {@link BasicLazyFunctionalityTest class documentation}
     *                        for information).
     */
    public static void lazyNullCallsOnceTest(Function<Supplier<Integer>, Lazy<Integer>> lazyTransformer) {
        CallCounter<Integer> callCounter = new CallCounter<>(null);
        Lazy<Integer> lazy = lazyTransformer.apply(callCounter);
        assertEquals(null, lazy.get());
        assertEquals(null, lazy.get());
        assertEquals(1, callCounter.getCallCount());
    }

    /**
     * Checks that returned Lazy object does not call the supplier if the value isn't requested.
     *
     * @param lazyTransformer The transformer to be tested (see {@link BasicLazyFunctionalityTest class documentation}
     *                        for information).
     */
    public static void lazyNotCallingSupplierTest(Function<Supplier<Integer>, Lazy<Integer>> lazyTransformer) {
        CallCounter<Integer> callCounter = new CallCounter<>(179);
        Lazy<Integer> lazy = lazyTransformer.apply(callCounter);
        assertEquals(0, callCounter.getCallCount());
    }
}
