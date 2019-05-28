package dev.pelgero.result;

import java.util.function.*;

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


    <U> Result<E, U> and(Result<E, U> andResult);

    <F> Result<F, T> or(Result<F, T> orResult);

    <F> Result<F, T> orElse(Function<E, Result<F, T>> mappingFn);

    default Result<E, T> inspect(Consumer<Result<E, T>> consumer) {
        consumer.accept(this);
        return this;
    }

    class IllegalUnwrap extends RuntimeException {
        IllegalUnwrap(String msg) {
            super(msg);
        }

        IllegalUnwrap(Throwable throwable) {
            super(throwable);
        }
    }

}