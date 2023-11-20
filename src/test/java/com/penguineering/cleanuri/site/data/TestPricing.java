package com.penguineering.cleanuri.site.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestPricing {

    @ParameterizedTest
    @CsvSource({
            "1, 10.00",
            "3, 10.00",
            "5, 9.00",
            "10, 8.50",
            "15, 8.50",
            "20, 7.00",
            "25, 7.00"
    })
    public void testFindDiscountedUnitPrice(int quantity, String expectedPrice) {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .addDiscount(1, new BigDecimal("10.00"))
                .addDiscount(5, new BigDecimal("9.00"))
                .addDiscount(10, new BigDecimal("8.50"))
                .addDiscount(20, new BigDecimal("7.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertEquals(
                new BigDecimal(expectedPrice),
                pricing.findDiscountedUnitPrice(quantity).orElse(null)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, ''",
            "3, ''",
            "5, 9.00"
    })
    public void testFindDiscountedUnitPriceWithoutUnitPrice(int quantity, String expectedPrice) {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .addDiscount(5, new BigDecimal("9.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertEquals(
                expectedPrice.isEmpty() ? Optional.empty() : Optional.of(new BigDecimal(expectedPrice)),
                pricing.findDiscountedUnitPrice(quantity)
        );

        assertNull(pricing.getUnitPrice().orElse(null));
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10.00",
            "3, 10.00"
    })
    public void testFindReducedUnitPriceWithOnlyUnitPrice(int quantity, String expectedPrice) {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .setUnitPrice(new BigDecimal("10.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertEquals(
                new BigDecimal(expectedPrice),
                pricing.findDiscountedUnitPrice(quantity).orElse(null)
        );

        assertEquals(
                new BigDecimal("10.00"),
                pricing.getUnitPrice().orElse(null)
        );
    }

    @Test
    public void testNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> new Pricing.Builder()
                .addDiscount(-5, new BigDecimal("9.00"))
                .build());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10.00",
            "3, 30.00",
            "5, 45.00",
            "10, 85.00",
            "15, 127.50",
            "20, 140.00",
            "25, 175.00"
    })
    public void testCalculateTotalPrice(int quantity, String expectedTotalPrice) {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .setUnitPrice(new BigDecimal("10.00"))
                .addDiscount(5, new BigDecimal("9.00"))
                .addDiscount(10, new BigDecimal("8.50"))
                .addDiscount(20, new BigDecimal("7.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertEquals(
                new BigDecimal(expectedTotalPrice),
                pricing.calculateTotalPrice(quantity).orElse(null)
        );
    }

    @Test
    public void testPriceBelowDiscountQuantity() {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .addDiscount(5, new BigDecimal("9.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertFalse(pricing.findDiscountedUnitPrice(4).isPresent());
        assertFalse(pricing.calculateTotalPrice(4).isPresent());
    }

    @Test
    public void testSanePricing() {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .addDiscount(5, new BigDecimal("9.00"))
                .addDiscount(10, new BigDecimal("8.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertTrue(pricing.areDiscountsSane());
    }

    @Test
    public void testNotSanePricing() {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .addDiscount(5, new BigDecimal("9.00"))
                .addDiscount(10, new BigDecimal("10.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        assertFalse(pricing.areDiscountsSane());
    }

    @Test
    public void testStreamDiscounts() {
        // Test with no discounts
        Optional<Pricing> optionalPricing1 = new Pricing.Builder().build();

        assertFalse(optionalPricing1.isPresent(), "Pricing should be empty when no unit price or discounts are added");

        // Test with some discounts
        Optional<Pricing> optionalPricing2 = new Pricing.Builder()
                .setUnitPrice(new BigDecimal("10.00"))
                .addDiscount(5, new BigDecimal("9.00"))
                .addDiscount(10, new BigDecimal("8.00"))
                .build();

        assertTrue(optionalPricing2.isPresent());

        Pricing pricing2 = optionalPricing2.get();

        assertTrue(pricing2.getUnitPrice().isPresent());
        assertEquals(new BigDecimal("10.00"), pricing2.getUnitPrice().get(), "Unit price should be 10.00");

        List<Pricing.Discount> discounts = pricing2.streamDiscounts().toList();
        assertFalse(discounts.isEmpty(), "Discounts stream should not be empty when discounts are added");
        assertEquals(2, discounts.size(), "Discounts stream should contain two items when unit price and two discounts are added");
        assertEquals(new Pricing.Discount(5, new BigDecimal("9.00")), discounts.get(0), "First discount should be (5, 9.00)");
        assertEquals(new Pricing.Discount(10, new BigDecimal("8.00")), discounts.get(1), "Second discount should be (10, 8.00)");
    }

    @Test
    public void testSerialization() throws Exception {
        Optional<Pricing> optionalPricing = new Pricing.Builder()
                .setUnitPrice(new BigDecimal("10.00"))
                .addDiscount(5, new BigDecimal("9.00"))
                .build();

        assertTrue(optionalPricing.isPresent());

        Pricing pricing = optionalPricing.get();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        String json = mapper.writeValueAsString(pricing);

        assertTrue(json.contains("\"unit_price\":10.00"));
        assertTrue(json.contains("\"discounts\":[{\"quantity\":5,\"unit_price\":9.00}]"));
        assertFalse(json.contains("\"discounts\":[{\"quantity\":1,\"unit_price\":10.00}]"));
    }

    @Test
    public void testDeserialization() throws Exception {
        String json = """
            {
              "unit_price": 10.00,
              "discounts": [
                {
                  "quantity": 5,
                  "unit_price": 9.00
                }
              ]
            }
            """;
        ObjectMapper mapper = new ObjectMapper();
        Pricing pricing = mapper.readValue(json, Pricing.class);

        assertNotNull(pricing, "Pricing should not be null");

        assertEquals(1, pricing.streamDiscounts().count());

        assertEquals(new BigDecimal("10.00"), pricing.getUnitPrice().orElse(null));

        assertTrue(pricing.streamDiscounts()
                .anyMatch(discount -> discount.equals(new Pricing.Discount(5, new BigDecimal("9.00")))));
        assertFalse(pricing.streamDiscounts()
                .anyMatch(discount -> discount.equals(new Pricing.Discount(1, new BigDecimal("10.00")))));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDiscountArguments")
    public void testInvalidDiscountArguments(Integer quantity, BigDecimal unitPrice, Class<? extends Throwable> expectedException) {
        assertThrows(expectedException, () -> new Pricing.Discount(quantity, unitPrice));
    }

    private static Stream<Arguments> provideInvalidDiscountArguments() {
        return Stream.of(
                Arguments.of(null, new BigDecimal("10.00"), NullPointerException.class),
                Arguments.of(-1, new BigDecimal("10.00"), IllegalArgumentException.class),
                Arguments.of(0, new BigDecimal("10.00"), IllegalArgumentException.class),
                Arguments.of(1, null, NullPointerException.class),
                Arguments.of(1, new BigDecimal("-10.00"), IllegalArgumentException.class)
        );
    }

}