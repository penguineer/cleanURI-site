package com.penguineering.cleanuri.site;

import java.net.URI;
import java.util.Optional;

/**
 * Describes a site with various attributes.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * SiteDescriptor siteDescriptor = new SiteDescriptor.Builder("label")
 *     .description("description")
 *     .site(URI.create("http://example.com"))
 *     .author("author")
 *     .license("license")
 *     .build();
 * }
 * </pre>
 * </p>
 */
public class SiteDescriptor {
    /**
     * The label of the site. This will be shown whenever the module is referenced.
     */
    private final String label;
    private final String description;
    private final URI site;
    private final String author;
    private final String license;

    private SiteDescriptor(Builder builder) {
        this.label = builder.label;
        this.description = builder.description;
        this.site = builder.site;
        this.author = builder.author;
        this.license = builder.license;
    }

    public String getLabel() {
        return label;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<URI> getSite() {
        return Optional.ofNullable(site);
    }

    public Optional<String> getAuthor() {
        return Optional.ofNullable(author);
    }

    public Optional<String> getLicense() {
        return Optional.ofNullable(license);
    }

    /**
     * Builds SiteDescriptor instances.
     */
    public static class Builder {
        private final String label;
        private String description;
        private URI site;
        private String author;
        private String license;

        public Builder(String label) {
            this.label = label;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder site(URI site) {
            this.site = site;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder license(String license) {
            this.license = license;
            return this;
        }

        public SiteDescriptor build() {
            return new SiteDescriptor(this);
        }
    }
}