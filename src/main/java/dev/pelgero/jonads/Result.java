package dev.pelgero.jonads;

import java.util.function.*;

/**
 * Result<E, T> is the type used for returning and propagating errors. It is an interface with the implementations
 * Ok(T), representing success and containing a value, and Err(E), representing error and containing an error value.
 *
 * A Result is basically a specific variant of an "Either" monad, containing either an ok-value or an error-value.
 *
 * Choose to return a Result whenever errors are expected and recoverable, most prominently used for I/O.
 *
 * @param <E> the error value's type
 * @param <T> to ok value's type
 */
public interface Result<E, T> {

    /**
     * Basic factory method for wrapping an operation that might throw an (runtime) exception in a Result. If provided
     * operation returns normally, you'll get an Ok(T), but if operation throws an exception you'll get a Err(E)
     *
     * @param operation that might fail with an exception
     * @param <T> the expected (normal) result type of operation
     * @return the result of the operation, either Ok(T) or Err(E), where E is the thrown Exception of operation.
     */
    static <T> Result<Throwable, T> of(Supplier<T> operation) {
        try {
            return new Ok<>(operation.get());
        } catch (Throwable err) {
            return new Err<>(err);
        }
    }

    /**
     * Whether this Result is an Ok(T) or an Err(E)
     * @return <code>true</code> when Ok(T), <code>false</code> when Err(E)
     */
    boolean isOk();

    /**
     * Whether this Result is an Ok(T) or an Err(E)
     * @return <code>true</code> when Err(E), <code>false</code> when Ok(E)
     */
    boolean isErr();

    /**
     * Applies the mapping function to the Ok-value, i.e. transforms T -> U when it is an Ok. Does nothing
     * if this is an Err(E)
     *
     * @param mappingFn the value transforming function, applied to T
     * @param <U> the new value type
     * @return the new value wrapped in an Ok (i.e. Ok(U)), or the unchanged Err(E)
     */
    <U> Result<E, U> map(Function<T, U> mappingFn);


    /**
     * Applies the mapping function to the Err-value, i.e. transforms the value of Err(E) to Err(F). Does nothing
     * if this is an Ok(T).
     *
     * @param mappingFn the value transforming function, applied to E
     * @param <F> the new err-value type
     * @return the new value wrapped in an Err (i.e. Err(F)), or the unchanged Ok(T)
     */
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