package io.vertex.starter.input;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.data.DataProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class PeriodicPublisherVerticle extends AbstractVerticle {

  private String topicName = "defaultTopic";
  private DataProvider dataProvider;
  private long period = 3000;

  public PeriodicPublisherVerticle(String topicName, DataProvider dataProvider, long period) {
    this.topicName = topicName;
    this.dataProvider = dataProvider;
    this.period = period;
  }

  private class DataPublisher implements Handler<Long> {
    @Override
    public void handle(Long aLong) {
      Data data = dataProvider.provide(new JsonObject().put("timer", aLong));
      vertx.eventBus().publish(topicName, data.toJsonString());
      logger.info(data + " was published to " + topicName);
    }
  }

	@Override
	public void start(Future<Void> startFuture) {
    vertx.setPeriodic(period, new DataPublisher());
    logger.info("Publisher started.");
	}

	@Override
	public void stop(Future stopFuture) throws Exception {
 	}

  private final Logger logger = LoggerFactory.getLogger(PeriodicPublisherVerticle.class.getName());

}
