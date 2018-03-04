package ru.spbau.alferov.javahw.lazy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Tests the {@link LazyFactory#createMultiThreadLazy(Supplier)} functionality.</p>
 *
 * <p>Tests the {@link BasicLazyFunctionalityTest} functions and stress-tests the possible concurrency.</p>
 */
public class MultiThreadLazyTest {
    /**
     * Tests {@link LazyFactory#createMultiThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyReturnValueTest(Function)}.
     */
    @Test
    public void returnValueTest() {
        BasicLazyFunctionalityTest.lazyReturnValueTest(LazyFactory::createMultiThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createMultiThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyReturnNullTest(Function)}.
     */
    @Test
    public void returnNullTest() {
        BasicLazyFunctionalityTest.lazyReturnNullTest(LazyFactory::createMultiThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createMultiThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyCallsOnceTest(Function)}.
     */
    @Test
    public void callsOnceTest() {
        BasicLazyFunctionalityTest.lazyCallsOnceTest(LazyFactory::createMultiThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createMultiThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyNullCallsOnceTest(Function)}.
     */
    @Test
    public void nullCallsOnceTest() {
        BasicLazyFunctionalityTest.lazyNullCallsOnceTest(LazyFactory::createMultiThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createMultiThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyNotCallingSupplierTest(Function)}.
     */
    @Test
    public void notCallingSupplierTest() {
        BasicLazyFunctionalityTest.lazyNotCallingSupplierTest(LazyFactory::createMultiThreadLazy);
    }

    /**
     * This class provides a Thread for Runnable objects in which we throw AssertionErrors (JUnit5 ignores assertions in
     * non-main threads).
     */
    private static class TestingThread {
        /**
         * The running Thread.
         */
        @NotNull
        private Thread thread;

        /**
         * The AssertionError thrown by the thread (if any, else null).
         */
        @Nullable
        private AssertionError e = null;

        /**
         * Constructs the Thread. Does not start it.
         *
         * @param r The Runnable for the thread.
         */
        TestingThread(Runnable r) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.run();
                    } catch (AssertionError exc) {
                        e = exc;
                    }
                }
            });
        }

        /**
         * Starts the thread.
         */
        public void start() {
            thread.start();
        }

        /**
         * Joins the thread. Throws AssertionError if some has happened during the thread execution.
         */
        public void join() throws InterruptedException {
            thread.join();
            if (e != null)
                throw e;
        }
    };

    /**
     * <p>Stress-tests the MultiThreadLazy object with two threads and a hundred {@link Lazy#get()} requests from each.</p>
     *
     * <p>Doesn't make synchronized requests to {@link BasicLazyFunctionalityTest.CallCounter#get()} so might be more
     * useful than {@link MultiThreadLazyTest#hundredThreadsConcurrentTest()} sometimes.</p>
     */
    @RepeatedTest(100)
    public void twoThreadsConcurrentTest() throws Exception {
        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(() -> 179);
        Runnable theTest = () -> {
            for (int i = 0; i < 100; i++) {
                assertEquals(new Integer(179), lazy.get());
            }
        };
        TestingThread thread1 = new TestingThread(theTest);
        TestingThread thread2 = new TestingThread(theTest);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
    }

    /**
     * <p>Stress-tests the MultiThreadLazy object with a hundred threads and a hundred {@link Lazy#get()} requests from
     * each.</p>
     *
     * <p>Additionally checks that the Supplier was called exactly once.</p>
     */
    @RepeatedTest(100)
    public void hundredThreadsConcurrentTest() throws Exception {
        final int requestCount = 100;
        final int threadCount = 100;
        BasicLazyFunctionalityTest.CallCounter<Integer> callCounter = new BasicLazyFunctionalityTest.CallCounter<>(179);
        Lazy<Integer> lazy = LazyFactory.createMultiThreadLazy(callCounter);
        Runnable theTest = () -> {
            for (int i = 0; i < requestCount; i++) {
                assertEquals(new Integer(179), lazy.get());
            }
        };

        TestingThread[] threads = new TestingThread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new TestingThread(theTest);
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            threads[i].join();
        }

        assertEquals(1, callCounter.getCallCount());
    }
}