package ru.spbau.alferov.javahw.lazy;

import org.jetbrains.annotations.Nullable;

/**
 * <p>This provides an interface for lazy calculations.</p>
 *
 * <p>It is supposed that the calculation is done only once per
 * execution: after the first call of {@link #get() get()}.</p>
 *
 * <p>{@link LazyFactory LazyFactory} provides a default method to
 * generate Lazy objects from suppliers.</p>
 */
public interface Lazy<T> {
    /**
     * Get the calculated value. Note that the value can be null.
     */
    @Nullable T get();
}
