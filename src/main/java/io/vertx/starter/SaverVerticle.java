package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;

public class SaverVerticle<T> extends AbstractVerticle {
  public static final String DEFAULT_TOPIC = "defaultTopic";

  private String topicName;
  private DataSaver saver;
  private MessageConsumer<T> consumer;

  private SaverVerticle() {}

  public SaverVerticle(DataSaver<T> saver, String topicName) {
    this.topicName = topicName;
    this.saver = saver;
  }

  public SaverVerticle(DataSaver<T> saver) {
    this(saver, DEFAULT_TOPIC);
  }

  @Override
  public void start() throws Exception {
    consumer = vertx.eventBus().consumer(topicName, saver);
  }

  @Override
  public void stop() throws Exception {
    if (consumer.isRegistered()) {
      consumer.unregister();
    }

  }
}
