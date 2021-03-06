package ru.spbau.alferov.javahw.maybe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A class storing a value or nothing.
 * May be used as a safe replacement for null values.
 */
public class Maybe<T> {
    /**
     * The stored value (or null if nothing is stored).
     */
    @Nullable
    private T stored;

    /**
     * Constructor from a pointer.
     * Should not be used directly (use just() and nothing() instead).
     */
    private Maybe(@Nullable T toStore) {
        stored = toStore;
    }

    /**
     * Constructs a new non-empty value pointing to t.
     * t should not be null.
     */
    @NotNull
    public static <T> Maybe<T> just(@NotNull T t) {
        return new Maybe<>(t);
    }

    /**
     * This static field stores the nothing maybe -- to make it not created
     * every time it is needed.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    private static final Maybe nothingValue = new Maybe(null);

    /**
     * Returns an empty value. Performs an unchecked cast in its body so
     * there is no need in doing it
     */
    @NotNull
    public static <T> Maybe<T> nothing() {
        @SuppressWarnings("unchecked")
        Maybe<T> ret = (Maybe<T>)nothingValue;
        return ret;
    }

    /**
     * Returns a stored value.
     * @throws MaybeException in case no value is stored.
     */
    @NotNull
    public T get() throws MaybeException {
        if (stored == null)
            throw new MaybeException();
        return stored;
    }

    /**
     * Returns true iff something (e.g. not nothing) is stored.
     */
    public boolean isPresent() {
        return stored != null;
    }

    /**
     * Takes some function and maps it to the stored value.
     * Returns "nothing" in case nothing is stored and "just" mapped
     * value otherwise.
     */
    @NotNull
    public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        if (stored == null)
            return Maybe.nothing();
        else {
            return Maybe.just(mapper.apply(stored));
        }
    }
}
