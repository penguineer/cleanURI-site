package com.penguineering.cleanuri.site.data;

import java.net.URI;
import java.util.Optional;

/**
 * Represents a product description with an ID, name, and image.
 *
 * <p>This class uses the Builder pattern for construction and
 * returns {@link Optional} instances from its getter methods
 * to handle the possibility of null values.</p>
 *
 * <p>The Builder returns an empty Optional if none of the values are set.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * ProductDescription description = new ProductDescription.Builder()
 *     .setId("123")
 *     .setName("Product Name")
 *     .setImage(new URI("http://example.com/image.jpg"))
 *     .build()
 *     .orElseThrow(); // Throws NoSuchElementException if none of the values are set
 * </pre>
 */
public class ProductDescription {
    private final String id;
    private final String name;
    private final URI image;

    private ProductDescription(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.image = builder.image;
    }

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<URI> getImage() {
        return Optional.ofNullable(image);
    }

    public static class Builder {
        private String id;
        private String name;
        private URI image;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setImage(URI image) {
            this.image = image;
            return this;
        }

        public Optional<ProductDescription> build() {
            return (id == null && name == null && image == null)
                    ? Optional.empty()
                    : Optional.of(new ProductDescription(this));
        }
    }
}