package com.penguineering.cleanuri.site;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestAbstractSiteBase {
    private static class TestSite extends AbstractSiteBase {
        public TestSite(SiteDescriptor descriptor) {
            super(descriptor);
        }

        @Override
        public boolean canProcessURI(URI uri) {
            return false;
        }
    }

    private final SiteDescriptor descriptor = new SiteDescriptor.Builder("Test Site").build();

    @Test
    public void testGetSiteDescriptor() {
        TestSite site = new TestSite(descriptor);
        assertEquals(descriptor, site.getSiteDescriptor());
    }

    @Test
    public void testNewCanonizer() {
        TestSite site = new TestSite(descriptor);
        assertFalse(site.newCanonizer(URI.create("http://example.com")).isPresent());
    }

    @Test
    public void testNewExtractor() {
        TestSite site = new TestSite(descriptor);
        assertFalse(site.newExtractor(URI.create("http://example.com")).isPresent());
    }
}