package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Random;

public class PublisherVerticle extends AbstractVerticle {

	private long timerId = 0;
	private long counter = 0;

  private String topicName = "defaultTopic";
  private String id = PublisherVerticle.class.getCanonicalName();
  private Random rnd = new Random();
  private DataMessageCodec dmc = new DataMessageCodec();
  private DeliveryOptions options = new DeliveryOptions().setCodecName(dmc.name());

  public PublisherVerticle(String topicName, String id) {
    this.topicName = topicName;
    this.id = id;
  }

	private class RandomDataPublicator implements Handler<Long> {
    @Override
    public void handle(Long aLong) {
      int r = rnd.nextInt();
      vertx.eventBus().publish(topicName, new Data(r), options);
      logger.info(r + " was published to " + topicName);
    }
  }

	@Override
	public void start(Future<Void> startFuture) {
    vertx.eventBus().registerCodec(dmc);
    timerId = vertx.setPeriodic(3000, new RandomDataPublicator());
    logger.info("Publisher started.");
	}

	@Override
	public void stop(Future stopFuture) throws Exception {
    vertx.cancelTimer(timerId);
    vertx.eventBus().unregisterCodec(dmc.name());
	}

  private final Logger logger = LoggerFactory.getLogger(PublisherVerticle.class.getName());

}
