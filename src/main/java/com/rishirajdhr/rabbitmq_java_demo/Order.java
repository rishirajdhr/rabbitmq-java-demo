package com.rishirajdhr.rabbitmq_java_demo;

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
public record Order(UUID orderID, String customer, String item, int quantity, double amount,
                    Instant createdAt) {}
