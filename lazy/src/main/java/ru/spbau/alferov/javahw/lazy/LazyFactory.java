package ru.spbau.alferov.javahw.lazy;

import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides a factory for default Lazy&lt;T&gt; realizations.
 * Either a single-thread or multi-thread object can be created.
 */
public class LazyFactory {
    /**
     * Creates an instance of {@link SingleThreadLazy SingleThreadLazy} from given supplier.
     * Ought to be default single-thread realization of Lazy.
     */
    @NotNull
    public static <T> Lazy<T> createSingleThreadLazy(@NotNull Supplier<T> supplier) {
        return new SingleThreadLazy<>(supplier);
    }

    /**
     * Creates an instance of {@link MultiThreadLazy MultiThreadLazy} from given supplier.
     * Ought to be default multi-thread realization of Lazy.
     */
    @NotNull
    public static<T> Lazy<T> createMultiThreadLazy(@NotNull Supplier<T> supplier) {
        return new MultiThreadLazy<>(supplier);
    }
}
