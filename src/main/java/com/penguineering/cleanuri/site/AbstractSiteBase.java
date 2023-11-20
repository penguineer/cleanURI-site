package com.penguineering.cleanuri.site;

import java.net.URI;
import java.util.Optional;

/**
 * An abstract base class for implementing a site.
 *
 * <p>This class provides a basic implementation of the {@link Site} interface.
 * It holds a {@link SiteDescriptor} and provides default implementations for
 * the {@link Site#getSiteDescriptor()}, {@link Site#newCanonizer(URI)}, and
 * {@link Site#newExtractor(URI)} methods.</p>
 *
 * <p>Subclasses must implement the {@link Site#canProcessURI(URI)} method to
 * determine whether a given URI can be processed by the site. They may also
 * override the {@link Site#newCanonizer(URI)} and {@link Site#newExtractor(URI)}
 * methods to provide site-specific canonizers and extractors.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 * public class MySite extends AbstractSiteBase {
 *     public MySite() {
 *         super(new SiteDescriptor.Builder("MySite")
 *             .description("My site description.")
 *             .site(URI.create("https://www.mysite.com/"))
 *             .author("Author Name")
 *             .license("License")
 *             .build());
 *     }
 *
 *     {@code @Override}
 *     public boolean canProcessURI(URI uri) {
 *         // Implement site-specific URI processing logic here
 *     }
 *
 *     {@code @Override}
 *     public Optional&lt;Canonizer&gt; newCanonizer(URI uri) {
 *         // Optionally provide a site-specific Canonizer here
 *     }
 *
 *     {@code @Override}
 *     public Optional&lt;Extractor&gt; newExtractor(URI uri) {
 *         // Optionally provide a site-specific Extractor here
 *     }
 * }
 * </pre>
 */
public abstract class AbstractSiteBase implements Site {
    protected final SiteDescriptor descriptor;

    protected AbstractSiteBase(SiteDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public SiteDescriptor getSiteDescriptor() {
        return descriptor;
    }

    @Override
    public abstract boolean canProcessURI(URI uri);

    @Override
    public Optional<Canonizer> newCanonizer(URI uri) {
        return Optional.empty();
    }

    @Override
    public Optional<Extractor> newExtractor(URI uri) {
        return Optional.empty();
    }
}
