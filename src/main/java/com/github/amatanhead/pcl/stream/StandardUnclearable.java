package com.github.amatanhead.pcl.stream;


/**
 * Provides default implementations for clear/dirty state management and {@link #isClear()} method.
 * <p>
 * Subclasses should call {@link #markUnclear()} method in the beginning of the overloaded {@link #input()}.
 */
abstract class StandardUnclearable implements TokenStream {
    private boolean clear = true;

    @Override
    public boolean isClear() {
        return clear;
    }

    /**
     * Internal API: marks this class as unclear. Intended to be called in the beginning of the {@link #input()}
     * procedure by class users.
     */
    protected void markUnclear() {
        clear = false;
    }
}
