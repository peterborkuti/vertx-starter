package io.vertx.starter;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class DataSaver<T extends Data> extends AbstractDataProcessor<Message<T>> {
  public DataSaver(SQLClient sqlClient) {
    super(sqlClient, "insert into data values (?)");
  }

  @Override
  public void processData(SQLConnection connection, Message<T> data) {
    JsonArray params = data.body().toJsonArray();
    System.out.println("save" + data.body());

    connection.updateWithParams(SQL, params, res -> {
      if (!res.succeeded()) {
        logger.error("ERROR Update failed:", res.cause());
      }
    });
  }

  private final Logger logger = LoggerFactory.getLogger(DataSaver.class.getName());
}
