package io.vertx.starter;

import com.github.mauricio.async.db.mysql.message.client.HandshakeResponseMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

	private List<String> vertexId = new ArrayList<String>();
  private String consumerId;

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("MainVerticle started!");

    JsonObject mySQLClientConfig = new JsonObject()
      .put("username", "root")
      .put("database", "vertx")
      .put("password", "root");

    vertx.deployVerticle(new SaverVerticle(mySQLClientConfig, "mytopic"), id -> vertexId.add(id.result()));

		vertx.deployVerticle(new PublisherVerticle("mytopic", "pub1"), id -> vertexId.add(id.result()));
    vertx.deployVerticle(new PublisherVerticle("mytopic", "pub2"), id -> vertexId.add(id.result()));
    vertx.deployVerticle(new PublisherVerticle("mytopic", "pub3"), id -> vertexId.add(id.result()));
    vertx.deployVerticle(new PublisherVerticle("mytopic", "pub4"), id -> vertexId.add(id.result()));

    vertx.deployVerticle(new ConsumerVerticle("mytopic", "con1"), id -> vertexId.add(id.result()));

	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		System.out.println("MyVerticle stopped!");
		for (String id: vertexId) {
      vertx.undeploy(id);
    }
	}

}
