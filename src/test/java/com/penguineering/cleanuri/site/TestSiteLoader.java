package com.penguineering.cleanuri.site;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TestSiteLoader {
    private SiteLoader siteLoader;
    private ServiceLoader<Site> mockLoader;
    private Consumer<SiteDescriptor> mockConsumer;

    @BeforeEach
    public void setUp() {
        mockLoader = createMockServiceLoader();
        mockConsumer = createMockConsumer();
        siteLoader = new SiteLoader(mockLoader, mockConsumer);
    }

    @SuppressWarnings("unchecked")
    private ServiceLoader<Site> createMockServiceLoader() {
        return Mockito.mock(ServiceLoader.class);
    }

    @SuppressWarnings("unchecked")
    private Consumer<SiteDescriptor> createMockConsumer() {
        return Mockito.mock(Consumer.class);
    }

    @Test
    public void testSetupWithDefaultConstructor() {
        SiteLoader siteLoader = new SiteLoader();
        List<Site> sites = siteLoader.findSitesOnClasspath();

        // Assert that the list of sites is not null
        assertNotNull(sites);
    }

    @Test
    public void testSetupWithConsumerConstructor() {
        Consumer<SiteDescriptor> mockConsumer = createMockConsumer();
        SiteLoader siteLoader = new SiteLoader(mockConsumer);
        List<Site> sites = siteLoader.findSitesOnClasspath();

        // Assert that the list of sites is not null
        assertNotNull(sites);

        // Verify that the consumer's accept method was called for each site
        sites.forEach(site -> verify(mockConsumer).accept(site.getSiteDescriptor()));
    }

    @Test
    public void testFindSitesOnClasspath() {
        Site mockSite = Mockito.mock(Site.class);
        SiteDescriptor mockDescriptor = Mockito.mock(SiteDescriptor.class);
        when(mockSite.getSiteDescriptor()).thenReturn(mockDescriptor);
        when(mockLoader.spliterator()).thenReturn(Collections.singletonList(mockSite).spliterator());

        List<Site> sites = siteLoader.findSitesOnClasspath();

        assertEquals(1, sites.size());
        assertEquals(mockSite, sites.get(0));

        verify(mockConsumer).accept(mockDescriptor);
    }

    @Test
    public void testClearCache() {
        Site mockSite = Mockito.mock(Site.class);
        when(mockLoader.spliterator()).thenAnswer(invocation -> Collections.singletonList(mockSite).spliterator());

        List<Site> sites = siteLoader.findSitesOnClasspath();
        assertEquals(1, sites.size());
        assertEquals(mockSite, sites.get(0));

        siteLoader.clearCache();

        sites = siteLoader.findSitesOnClasspath();
        assertEquals(1, sites.size());
        assertEquals(mockSite, sites.get(0));

        verify(mockLoader, times(2)).spliterator();
    }
}