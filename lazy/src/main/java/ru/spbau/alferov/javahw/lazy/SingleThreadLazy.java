package ru.spbau.alferov.javahw.lazy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * <p>This is the non-thread-safe realization of Lazy&lt;T&gt; class.
 * It is generally faster then the thread-safe version but provides
 * less functionality.</p>
 *
 * <p>The value is returned by some supplier. It is guaranteed that
 * the supplier will be called no more than once per execution and
 * it will not be called unless the get() function is called at
 * least once.</p>
 *
 * <p>The reference to supplier is destroyed after the supplier is
 * called.</p>
 */
public class SingleThreadLazy<T> implements Lazy<T> {
    /**
     * This stores the supplier from which the object should be generated
     * OR null if the object is already generated.
     * As long as the supplier might be heavy, we don't need to store the
     * reference after the supplier was called. Additionally, checking
     * tSupplier on being null is the way to check if the supplier has
     * already been called.
     */
    @Nullable
    private Supplier<T> tSupplier;

    /**
     * Here we store the value if the supplier has already been called OR
     * null otherwise. Note that even if supplier has been called, the
     * value can still be null (in case the supplier returned null).
     */
    @Nullable
    private T value = null;

    /**
     * This is the basic constructor.
     * @param supplier Supplier which generates the value to be taken.
     */
    SingleThreadLazy(@NotNull Supplier<T> supplier) {
    }

    /**
     * Returns the value returned by the supplier.
     */
    @Override
    @Nullable
    public T get() {
        return null;
    }
}
