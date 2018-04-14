package io.vertx.starter;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

public class Util {
  public static void printMessage(Message<Object> event) {
    System.out.println("received message: "
      + ((Data) event.body()));
  }
}
