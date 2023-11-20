package com.penguineering.cleanuri.site;

import java.util.function.BiConsumer;
import java.util.logging.Level;

/**
 * Interface for classes that need to pass exceptions to a handler.
 *
 * <p>This interface provides a method to set an exception handler that will be called
 * when an exception occurs. The handler is a {@link BiConsumer} that takes a {@link Level}
 * and a {@link Throwable}.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * MyClass myClass = new MyClass();
 * myClass.withExceptionHandler((level, throwable) -> {
 *     // Handle exception here
 * });
 * </pre>
 *
 * @see java.util.function.BiConsumer
 * @see java.util.logging.Level
 */
public interface ExceptionPassing {
    /**
     * Sets the exception handler for this object.
     *
     * @param exceptionHandler a BiConsumer that takes a Level and a Throwable
     * @return the object itself for method chaining
     */
    ExceptionPassing withExceptionHandler(BiConsumer<Level, Throwable> exceptionHandler);
}