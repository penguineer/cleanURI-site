package com.penguineering.cleanuri.site;

import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * Class for loading site implementations available on the classpath.
 * <p>
 * This class uses the Java ServiceLoader mechanism to find and load all available
 * implementations of the Site interface.
 * </p>
 * <p>
 * To configure an implementation for discovery by the ServiceLoader, you need to create a provider-configuration
 * file in the META-INF/services directory of your JAR file. The name of this file should be the fully-qualified
 * class name of the Site interface (com.penguineering.cleanuri.site.Site). The file should contain the
 * fully-qualified class name of your implementations, one per line.
 * </p>
 */
public class SiteLoader {
    private List<Site> sites = null;
    private final ServiceLoader<Site> loader;
    private final Consumer<SiteDescriptor> siteReporter;

    /**
     * Default constructor for normal use.
     * <p>
     * This constructor initializes the ServiceLoader for loading Site implementations.
     * The siteReporter will do nothing.
     * </p>
     */
    public SiteLoader() {
        this(ServiceLoader.load(Site.class), null);  // Initialize with null
    }

    /**
     * Default constructor for normal use.
     * <p>
     * This constructor initializes the ServiceLoader for loading Site implementations.
     * It also accepts a Consumer that is called for each site found with the site's descriptor.
     * </p>
     *
     * @param siteReporter a Consumer that is called for each site found
     */
    public SiteLoader(Consumer<SiteDescriptor> siteReporter) {
        this(ServiceLoader.load(Site.class), siteReporter);
    }

    /**
     * Constructor for testing.
     * <p>
     * This constructor allows a ServiceLoader to be passed in, enabling control over the loading of Site implementations in tests.
     * It also accepts a Consumer that is called for each site found with the site's descriptor.
     * </p>
     *
     * @param loader the ServiceLoader to use for loading Site implementations
     * @param siteReporter a Consumer that is called for each site found
     */
    SiteLoader(ServiceLoader<Site> loader, Consumer<SiteDescriptor> siteReporter) {
        this.loader = loader;
        this.siteReporter = siteReporter != null ? siteReporter : s -> {};  // Initialize with the provided consumer or a no-operation consumer
    }

    /**
     * Finds and loads all Site implementations available on the classpath.
     * <p>
     * This method uses the Java ServiceLoader mechanism to find and load all available
     * implementations of the Site interface. The loaded sites are cached, so subsequent
     * calls to this method return the cached list. For each site found, the siteReporter
     * member variable is called with the site's descriptor.
     * </p>
     *
     * @return a List of all loaded Site implementations
     */
    public synchronized List<Site> findSitesOnClasspath() {
        if (sites == null) {
            sites = StreamSupport.stream(loader.spliterator(), false)
                    .peek(site -> siteReporter.accept(site.getSiteDescriptor()))  // Call the siteReporter consumer for each site
                    .toList();
        }
        return sites;
    }

    /**
     * Clears the cache of loaded Site implementations.
     * <p>
     * After calling this method, the next call to {@link #findSitesOnClasspath()} will reload the Site implementations.
     * </p>
     */
    public synchronized void clearCache() {
        sites = null;
    }
}