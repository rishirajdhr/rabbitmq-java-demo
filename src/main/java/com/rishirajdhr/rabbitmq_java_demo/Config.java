package com.rishirajdhr.rabbitmq_java_demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configures application properties.
 */
public class Config {
  /** The object mapper to use for dealing with order JSON payloads. */
  public static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  /** The host for the message queue service. */
  public static final String QUEUE_HOST = "localhost";

  /** The name of the message queue used in the application. */
  public static final String QUEUE_NAME = "order";
}
