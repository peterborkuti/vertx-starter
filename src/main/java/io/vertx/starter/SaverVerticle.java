package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class SaverVerticle<T extends Data> extends AbstractVerticle {
  public static final String DEFAULT_TOPIC = "defaultTopic";

  private String topicName;
  private DataSaver<T> saver;
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
    logger.info("Start and consume messages from " + topicName);
  }

  @Override
  public void stop() throws Exception {
    if (consumer.isRegistered()) {
      consumer.unregister();
    }

  }

  private final Logger logger = LoggerFactory.getLogger(SaverVerticle.class.getName());
}
