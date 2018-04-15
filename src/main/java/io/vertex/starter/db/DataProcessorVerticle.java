package io.vertex.starter.db;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class DataProcessorVerticle extends AbstractVerticle {
  public static final String DEFAULT_TRIGGER_TOPIC = "dataTrigger";

  private String triggerTopic;
  private AbstractDataProcessor dataProcessor;

  private DataProcessorVerticle() {}

  public DataProcessorVerticle(AbstractDataProcessor dataProcessor, String triggerTopicName) {
    this.triggerTopic = triggerTopicName;
    this.dataProcessor = dataProcessor;
  }

  public DataProcessorVerticle(AbstractDataProcessor dataProcessor) {
    this(dataProcessor, DEFAULT_TRIGGER_TOPIC);
  }

  @Override
  public void start() throws Exception {
    MessageConsumer consumer = vertx.eventBus().consumer(triggerTopic, dataProcessor);
    logger.info("Start and consume messages from " + triggerTopic);
  }

  @Override
  public void stop() throws Exception {
  }

  private final Logger logger = LoggerFactory.getLogger(DataProcessorVerticle.class.getName());
}
