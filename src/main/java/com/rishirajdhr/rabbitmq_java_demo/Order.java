package com.rishirajdhr.rabbitmq_java_demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents an order in the application.
 *
 * @param orderID the order ID
 * @param customer the username of the customer
 * @param item the name of the item
 * @param quantity the quantity of the item that is purchased
 * @param amount the total order amount value
 * @param createdAt the order creation timestamp
 */
public record Order(
    @JsonProperty("order_id") UUID orderID,
    @JsonProperty("customer") String customer,
    @JsonProperty("item") String item,
    @JsonProperty("quantity") int quantity,
    @JsonProperty("amount") double amount,
    @JsonProperty("created_at") Instant createdAt
) {
  @JsonCreator
  public Order(
      @JsonProperty("customer") String customer,
      @JsonProperty("item") String item,
      @JsonProperty("quantity") int quantity,
      @JsonProperty("amount") double amount
  ) {
    this(UUID.randomUUID(), customer, item, quantity, amount, Instant.now());
  }
}
