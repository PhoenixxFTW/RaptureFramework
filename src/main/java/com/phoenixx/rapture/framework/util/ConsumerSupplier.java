package com.phoenixx.rapture.framework.util;

/**
 * @author Phoenixx
 * @project RaptureFramework
 * @since 9:02 PM [18-05-2022]
 */
@FunctionalInterface
public interface ConsumerSupplier<T, V> {

    /**
     * Performs this operation on the given argument and returns the other value.
     *
     * @param t the input argument
     */
    V accept(T t);
}
