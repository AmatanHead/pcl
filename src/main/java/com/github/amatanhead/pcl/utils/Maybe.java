package com.github.amatanhead.pcl.utils;

import java.util.Objects;
import java.util.function.Function;

/**
 * A container which can hold a value or its absence.
 *
 * @param <T> type of a stored value.
 */
public class Maybe<T> {
    private final T val;
    private final boolean has;

    /**
     * Construct an empty container.
     */
    public Maybe() {
        this.val = null;
        this.has = false;
    }

    /**
     * Construct a container which holds the given value.
     */
    public Maybe(T val) {
        this.val = val;
        this.has = true;
    }

    /**
     * Get stored value.
     *
     * @return if container is not empty, returns stored value, otherwise returns `null`.
     */
    public T getVal() {
        return val;
    }

    /**
     * Check if there is a stored value in this container.
     *
     * @return true if there is a stored value.
     */
    public boolean hasVal() {
        return has;
    }

    /**
     * Map value of this maybe into a new maybe, performing monadic calculations.
     *
     * @param function mapper.
     * @param <R>      output type of the mapper.
     * @return if container is not empty, returns a new maybe containing the result of `function` applied to the value
     * of this container. If container is empty, returns a new empty container.
     */

    public <R> Maybe<R> map(Function<T, R> function) {
        if (!hasVal()) {
            return new Maybe<>();
        } else {
            return new Maybe<>(function.apply(getVal()));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maybe<?> maybe = (Maybe<?>) o;
        if (has) {
            return maybe.has && Objects.equals(val, maybe.val);
        } else {
            return !maybe.has;
        }
    }

    @Override
    public int hashCode() {
        if (has) {
            return Objects.hash(null, true);
        } else {
            return Objects.hash(val, false);
        }
    }
}
