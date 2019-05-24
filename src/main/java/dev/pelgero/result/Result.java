package dev.pelgero.result;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Result<E, T> {

    static <T> Result<Throwable, T> of(Supplier<T> operation) {
        try {
            return new Ok<>(operation.get());
        } catch (Throwable err) {
            return new Err<>(err);
        }
    }

    boolean isOk();
    boolean isErr();

    <U> Result<E, U> map(Function<T, U> mappingFn);
    <F> Result<F, T> mapErr(Function<E, F> mappingFn);

    <U> Result<E, U> andThen(Function<T, Result<E, U>> mappingFn);

    T unwrap();
    E unwrapErr();
    T unwrapOr(T or);
    T unwrapOrElse(Function<E, T> elseFn);

    class IllegalUnwrap extends RuntimeException {
        public IllegalUnwrap(String msg) {
            super(msg);
        }

        public IllegalUnwrap(Throwable throwable) {
            super(throwable);
        }
    }

}