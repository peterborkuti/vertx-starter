package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

	private List<String> vertexId = new ArrayList<String>();
  private SQLClient sqlClientForSaving;
  private SQLClient sqlClientForListing;

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("MainVerticle started!");

    JsonObject mySQLClientConfig = new JsonObject()
      .put("username", "root")
      .put("database", "vertx")
      .put("password", "root");

    sqlClientForSaving = MySQLClient.createNonShared(vertx, mySQLClientConfig);
    sqlClientForListing = MySQLClient.createNonShared(vertx, mySQLClientConfig);

    DataSaver<Data> dataSaver = new DataSaver<>(sqlClientForSaving);
    DataLister<Data> dataLister = new DataLister<>(sqlClientForListing, new ListerStreamHandler());

    vertx.deployVerticle(new SaverVerticle(dataSaver, "mytopic"), id -> vertexId.add(id.result()));
    //vertx.deployVerticle(new ListerVerticle(dataLister), id -> vertexId.add(id.result()));

		//vertx.deployVerticle(new PublisherVerticle("mytopic", "pub1"), id -> vertexId.add(id.result()));
    //vertx.deployVerticle(new PublisherVerticle("mytopic", "pub2"), id -> vertexId.add(id.result()));
    //vertx.deployVerticle(new PublisherVerticle("mytopic", "pub3"), id -> vertexId.add(id.result()));
    vertx.deployVerticle(new PublisherVerticle("mytopic", "pub4"), id -> vertexId.add(id.result()));

    //vertx.deployVerticle(new ConsumerVerticle("mytopic", "con1"), id -> vertexId.add(id.result()));
	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		System.out.println("MyVerticle stopped!");

		sqlClientForListing.close();
		sqlClientForSaving.close();

		for (String id: vertexId) {
      vertx.undeploy(id);
    }
	}

}
