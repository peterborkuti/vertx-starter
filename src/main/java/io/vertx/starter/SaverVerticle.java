package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class SaverVerticle extends AbstractVerticle {
  public static final String INSERT="insert into data values (?)";

  private JsonObject dbConfig = null;
  private SQLClient sqlClient = null;
  private String topicName = "defaultTopic";

  public SaverVerticle(JsonObject dbConfig, String topicName) {
    this.dbConfig = dbConfig;
    this.topicName = topicName;
  }

  private void saveData(SQLConnection connection, String data) {
    JsonArray params = new JsonArray().add(data);

    connection.updateWithParams(INSERT, params, res -> {
      if (!res.succeeded()) {
        System.out.println("Error inserting data:" + data);
      }
    });
  }

  private void save(String data) {
    sqlClient.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection connection = res.result();

        saveData(connection, data);

        connection.close();
      } else {
        System.out.println("Error: got no connection");
      }
    });
  }

  @Override
  public void start() throws Exception {
    sqlClient = MySQLClient.createShared(vertx, dbConfig);
    sqlClient.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection connection = res.result();
        vertx.eventBus().consumer(topicName, message -> save(message.body().toString()) );
        connection.close();
      } else {
        System.out.println("Error: got no connection");
      }
    });

  }

  @Override
  public void stop() throws Exception {
    sqlClient.close();
  }
}
