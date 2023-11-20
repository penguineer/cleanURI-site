package com.penguineering.cleanuri.site;

import java.net.URI;
import java.util.Optional;

/**
 * Interface for a URI canonizer.
 * <p>
 * A URI canonizer is responsible for transforming URIs into a canonical form.
 * This process removes redundant information and ensures that URIs can be compared effectively.
 * </p>
 */
public interface Canonizer extends ExceptionPassing {
    /**
     * Transforms the provided URI into its canonical form.
     * <p>
     * Two URIs are considered equal if the String representation of their canonical forms are equal.
     * </p>
     *
     * @return The canonical form of the URI.
     * @throws NullPointerException If the URI argument is null.
     * @throws IllegalArgumentException If the URI cannot be canonized by the canonizer.
     */
    Optional<URI> canonize();
}