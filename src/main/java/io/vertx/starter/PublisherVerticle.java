package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;

import java.util.Random;

public class PublisherVerticle extends AbstractVerticle {

	private long timerId = 0;
	private long counter = 0;

  private String topicName = "defaultTopic";
  private String id = PublisherVerticle.class.getCanonicalName();
  private Random rnd = new Random();

  public PublisherVerticle(String topicName, String id) {
    this.topicName = topicName;
    this.id = id;
  }

	private class RandomGenerator implements Handler<Long> {
    @Override
    public void handle(Long aLong) {
      vertx.eventBus().publish(topicName, rnd.nextInt());
      System.out.println(id + ": " + counter);
    }
  }

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("Publisher started!");
    timerId = vertx.setPeriodic(3000, new RandomGenerator());
    System.out.println("Timer started!");
	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		System.out.println("Publisher stopped!");
    vertx.cancelTimer(timerId);
    System.out.println("Timer stopped!");
	}

}
