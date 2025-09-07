package com.rishirajdhr.rabbitmq_java_demo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class OrderProducer {
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage: java OrderProducer --orders <orders-json-file>");
      return;
    }

    if (!args[0].equals("--orders")) {
      System.out.println("Unknown option: " + args[0]);
    }

    List<Order> orders = extractOrders(args);
    sendOrdersToQueue(orders);
  }

  private static List<Order> extractOrders(String[] args) throws IOException {
    String ordersFilename = args[1];
    File ordersFile = new File(ordersFilename);
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(ordersFile, new TypeReference<List<Order>>() { });
  }

  private static void sendOrdersToQueue(List<Order> orders) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(Config.QUEUE_HOST);

    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
      String queue = Config.QUEUE_NAME;
      boolean durable = false;
      boolean exclusive = false;
      boolean autoDelete = false;
      Map<String, Object> arguments = null;

      channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);

      ObjectMapper objectMapper = new ObjectMapper();
      for (Order order : orders) {
        String serializedOrder = objectMapper.writeValueAsString(order);
        channel.basicPublish("", queue, null, serializedOrder.getBytes(StandardCharsets.UTF_8));
        System.out.printf("Sent order for %d units of %s by %s%n", order.quantity(), order.item(),
                          order.customer());
      }
    }
  }
}
