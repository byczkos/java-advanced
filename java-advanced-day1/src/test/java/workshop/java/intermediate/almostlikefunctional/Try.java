package workshop.java.intermediate.almostlikefunctional;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Try<T> {

    @FunctionalInterface
    interface ThrowingSupplier<T> {
        T get() throws Throwable;
    }

    static <Input> Supplier<Try<Input>> supplier(ThrowingSupplier<Input> supplier) {
        return () -> tryIt(supplier);
    }

    static <Input> Try<Input> tryIt(ThrowingSupplier<Input> supplier) {
        // TODO start point of $ExitTest
        return null;
    }

    private Try() {
    }

    public abstract boolean isSuccess();

    public abstract T recover(Function<? super Throwable, T> f);

    public abstract T getNullable();

    public abstract Optional<T> getOptional();

    public abstract T getOrThrow() throws Throwable;

    public abstract T getOrThrowUnchecked();

    public abstract T orElse(T def);

    public abstract T orElseGet(Supplier<T> defSupplier);

    public abstract <U> Try<U> map(Function<T, U> f);


    //<U> Try<U> flatMap(Function<T, Try<U>> f);
}
