package com.penguineering.cleanuri.site.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents pricing information for an item, including unit price and quantity-based discounts.
 *
 * <p>Example usage:</p>
 * <pre>
 * Pricing pricing = new Pricing.Builder()
 *     .setUnitPrice(new BigDecimal("10.00"))
 *     .addDiscount(5, new BigDecimal("9.00"))
 *     .build();
 * </pre>
 */
@JsonDeserialize(builder = Pricing.Builder.class)
public class Pricing {
    /**
     * A record representing a quantity-based discount in unit price.
     *
     * @param quantity  the quantity at which the discount applies
     * @param unitPrice the discounted unit price for the specified quantity
     */
    public record Discount(@JsonProperty("quantity") Integer quantity,
                           @JsonProperty("unit_price") BigDecimal unitPrice) {
        @JsonCreator
        public Discount {
            Objects.requireNonNull(quantity, "Quantity cannot be null");
            if (quantity <= 0)
                throw new IllegalArgumentException("Quantity must be greater than zero");

            Objects.requireNonNull(unitPrice, "Unit price cannot be null");
            if (unitPrice.compareTo(BigDecimal.ZERO) < 0)
                throw new IllegalArgumentException("Unit price cannot be negative");
        }
    }

    /**
     * A list of Discount objects.
     * The list is sorted in ascending order by quantity.
     */
    private final List<Discount> discounts;

    /**
     * The unit price of the item. This is stored separately from the discounts list for efficiency.
     * It provides quick access to the price when the quantity is 1 or when no applicable discount is found,
     * thereby eliminating the need to traverse the discounts list in these scenarios.
     */
    private final BigDecimal unitPrice;

    private Pricing(Builder builder) {
        this.discounts = builder.discounts.values().stream()
                .sorted(Comparator.comparing(Discount::quantity))
                .collect(Collectors.toList());

        Discount unitPriceDiscount = builder.discounts.get(1);
        this.unitPrice = unitPriceDiscount != null ? unitPriceDiscount.unitPrice() : null;
    }

    /**
     * Return discounts, excluding the unit price for quantity 1.
     *
     * @return a Stream of Discount objects.
     */
    @JsonProperty("discounts")
    public Stream<Discount> streamDiscounts() {
        return discounts.stream()
                .filter(discount -> discount.quantity() > 1);
    }

    /**
     * Retrieves the base price per unit, excluding any discounts.
     *
     * @return Optional containing the unit price, if defined.
     */
    @JsonProperty("unit_price")
    public Optional<BigDecimal> getUnitPrice() {
        return Optional.ofNullable(unitPrice);
    }

    /**
     * Calculates the discounted unit price for a specified quantity, considering any
     * available discounts. The method identifies the largest discount that is less
     * than or equal to the given quantity and returns its unit price.
     *
     * @param quantity the quantity for which to calculate the discounted unit price
     * @return an Optional<BigDecimal> containing the discounted unit price, if available.
     */
    public Optional<BigDecimal> findDiscountedUnitPrice(Integer quantity) {
        return discounts.stream()
                .filter(discount -> discount.quantity().compareTo(quantity) <= 0)
                .reduce((first, second) -> second)
                .map(Discount::unitPrice);
    }

    /**
     * Computes the total cost for a specified quantity. The total cost is determined by multiplying
     * the quantity with the applicable discounted unit price, if available.
     *
     * @param quantity the quantity for which the total cost is to be calculated
     * @return an Optional<BigDecimal> containing the total cost. If no price can be determined for
     * the given quantity, an empty Optional is returned.
     */
    public Optional<BigDecimal> calculateTotalPrice(Integer quantity) {
        return findDiscountedUnitPrice(quantity)
                .map(reducedPrice -> reducedPrice.multiply(BigDecimal.valueOf(quantity)));
    }

    /**
     * Checks if the discounts are indeed reducing the unit price as the quantity increases.
     *
     * @return true if the discounts are sane, false otherwise
     */
    public boolean areDiscountsSane() {
        return discounts.stream()
                .map(Discount::unitPrice)
                .reduce((previousPrice, currentPrice) ->
                        currentPrice.compareTo(previousPrice) < 0 ? currentPrice : BigDecimal.valueOf(-1))
                .orElse(BigDecimal.ZERO)
                .compareTo(BigDecimal.valueOf(-1)) != 0;
    }

    @JsonPOJOBuilder(withPrefix = "", buildMethodName = "buildForDeserialization")
    public static class Builder {
        private final Map<Integer, Discount> discounts = new HashMap<>();

        @JsonSetter("unit_price")
        public Builder setUnitPrice(BigDecimal unitPrice) {
            addDiscount(1, unitPrice);
            return this;
        }

        public Builder addDiscount(Integer quantity, BigDecimal discountedPrice) {
            discounts.put(quantity, new Discount(quantity, discountedPrice));
            return this;
        }

        @JsonSetter("discounts")
        protected Builder setJsonDiscounts(List<Discount> discounts) {
            discounts.forEach(discount -> addDiscount(discount.quantity(), discount.unitPrice()));
            return this;
        }

        public Optional<Pricing> build() {
            return discounts.isEmpty()
                    ? Optional.empty()
                    : Optional.of(new Pricing(this));
        }

        protected Pricing buildForDeserialization() {
            return build().orElse(null);
        }
    }
}
