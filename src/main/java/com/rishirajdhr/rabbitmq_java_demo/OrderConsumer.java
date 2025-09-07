package com.rishirajdhr.rabbitmq_java_demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class OrderConsumer {
  public static void main(String[] args) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(Config.QUEUE_HOST);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String queue = Config.QUEUE_NAME;
    boolean durable = false;
    boolean exclusive = false;
    boolean autoDelete = false;
    Map<String, Object> arguments = null;

    channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
    System.out.println("Waiting for messages. To exit press Control-C.");

    channel.basicQos(1);

    DeliverCallback deliverCallback =
        (consumerTag, delivery) -> {
          String serializedOrder = new String(delivery.getBody(), StandardCharsets.UTF_8);
          try {
            processOrder(serializedOrder);
          } catch (IOException e) {
            throw new RuntimeException(e);
          } finally {
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          }
        };
    boolean autoAck = false;
    channel.basicConsume(queue, autoAck, deliverCallback, consumerTag -> { });
  }

  private static void processOrder(String serializedOrder) throws IOException {
    ObjectMapper objectMapper = Config.OBJECT_MAPPER;
    Order order = objectMapper.readValue(serializedOrder, Order.class);

    int consumerId = Integer.parseInt(System.getenv("CONSUMER_ID"));
    System.out.printf("[Consumer %d] Processed Order: %s, Created At: %s, %s has ordered %d " +
                          "units of %s for $%s%n", consumerId, order.orderID(), order.createdAt(),
                      order.customer(), order.quantity(), order.item(), order.amount());
  }
}
