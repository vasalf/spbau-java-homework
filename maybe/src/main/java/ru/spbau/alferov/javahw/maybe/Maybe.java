package ru.spbau.alferov.javahw.maybe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Maybe<T> {
    @Nullable
    private T stored;

    private Maybe(@Nullable T toStore) {
        stored = toStore;
    }

    @NotNull
    public static <T> Maybe<T> just(@NotNull T t) {
        return new Maybe<>(t);
    }

    @NotNull
    public static <T> Maybe<T> nothing() {
        return new Maybe<>(null);
    }

    public T get() throws MaybeException {
        if (stored == null)
            throw new MaybeException();
        return stored;
    }

    public boolean isPresent() {
        return stored != null;
    }

    public <U> Maybe<U> map(Function<? super T, ? extends U> mapper) {
        if (stored == null)
            return Maybe.nothing();
        else {
            return Maybe.just(mapper.apply(stored));
        }
    }
}
