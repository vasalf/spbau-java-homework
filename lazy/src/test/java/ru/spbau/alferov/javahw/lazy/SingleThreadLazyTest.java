package ru.spbau.alferov.javahw.lazy;

import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>Tests the {@link LazyFactory#createSingleThreadLazy(Supplier)} functionality.</p>
 *
 * <p>Actually, just calls the functions from {@link BasicLazyFunctionalityTest} class.</p>
 */
public class SingleThreadLazyTest {
    /**
     * Tests {@link LazyFactory#createSingleThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyReturnValueTest(Function)}.
     */
    @Test
    public void returnValueTest() {
        BasicLazyFunctionalityTest.lazyReturnValueTest(LazyFactory::createSingleThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createSingleThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyReturnNullTest(Function)}.
     */
    @Test
    public void returnNullTest() {
        BasicLazyFunctionalityTest.lazyReturnNullTest(LazyFactory::createSingleThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createSingleThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyCallsOnceTest(Function)}.
     */
    @Test
    public void callsOnceTest() {
        BasicLazyFunctionalityTest.lazyCallsOnceTest(LazyFactory::createSingleThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createSingleThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyNullCallsOnceTest(Function)}.
     */
    @Test
    public void nullCallsOnceTest() {
        BasicLazyFunctionalityTest.lazyNullCallsOnceTest(LazyFactory::createSingleThreadLazy);
    }

    /**
     * Tests {@link LazyFactory#createSingleThreadLazy(Supplier)} on
     * {@link BasicLazyFunctionalityTest#lazyNotCallingSupplierTest(Function)}.
     */
    @Test
    public void notCallingSupplierTest() {
        BasicLazyFunctionalityTest.lazyNotCallingSupplierTest(LazyFactory::createSingleThreadLazy);
    }
}