package com.penguineering.cleanuri.site;

import java.net.URI;
import java.util.Optional;

/**
 * Interface for a site.
 * <p>
 * A site is responsible for processing URIs and providing canonizers and extractors.
 * </p>
 */
public interface Site {
    /**
     * Returns the descriptor of the site.
     *
     * @return The site descriptor.
     */
    SiteDescriptor getSiteDescriptor();

    /**
     * Checks if the site can process the given URI.
     *
     * @param uri The URI to check.
     * @return true if the site can process the URI, false otherwise.
     */
    boolean canProcessURI(URI uri);

    /**
     * Creates a new canonizer for the given URI.
     *
     * @param uri The URI to canonize.
     * @return An Optional containing the canonizer if one could be created, or an empty Optional otherwise.
     */
    Optional<Canonizer> newCanonizer(URI uri);

    /**
     * Creates a new extractor.
     *
     * @return An Optional containing the extractor if one could be created, or an empty Optional otherwise.
     */
    Optional<Extractor> newExtractor(URI uri);
}