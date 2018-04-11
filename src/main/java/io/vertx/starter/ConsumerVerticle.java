package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

public class ConsumerVerticle extends AbstractVerticle {

  private String topicName = "defaultTopic";
	private String id = ConsumerVerticle.class.getCanonicalName();

	public ConsumerVerticle(String topicName, String id) {
	  this.topicName = topicName;
	  this.id = id;
  }

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("Consumer started!");

    vertx.eventBus().consumer(topicName,  Util::printMessage);
	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		System.out.println("Consumer stopped!");
	}

}
