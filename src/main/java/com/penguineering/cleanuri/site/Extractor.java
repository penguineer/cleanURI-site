package com.penguineering.cleanuri.site;

import com.penguineering.cleanuri.site.data.Pricing;
import com.penguineering.cleanuri.site.data.ProductDescription;

import java.util.Optional;

/**
 * Interface for an extractor.
 * <p>
 * An extractor is responsible for extracting specific information from a site.
 * </p>
 */
public interface Extractor extends ExceptionPassing {
    /**
     * Extracts the document title from the site.
     *
     * @return An Optional containing the document title if it could be extracted, or an empty Optional otherwise.
     */
    Optional<String> extractDocumentTitle();

    /**
     * Extracts the product description from the site.
     *
     * @return An Optional containing the product description if it could be extracted, or an empty Optional otherwise.
     */
    Optional<ProductDescription> extractProductDescription();

    /**
     * Extracts the pricing information from the site.
     *
     * @return An Optional containing the pricing information if it could be extracted, or an empty Optional otherwise.
     */
    Optional<Pricing> extractPricing();
}