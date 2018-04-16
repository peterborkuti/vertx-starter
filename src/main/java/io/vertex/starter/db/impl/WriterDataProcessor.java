package io.vertex.starter.db.impl;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.db.AbstractDataProcessor;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class WriterDataProcessor extends AbstractDataProcessor {
  public WriterDataProcessor(SQLClient sqlClient, String insertData) {
    super(sqlClient, insertData);
  }

  public void processData(SQLConnection connection, String data) {
    JsonArray params = Data.createFromJsonString(data).toJsonArray();
    System.out.println("save" + data);

    connection.updateWithParams(SQL, params, res -> {
      if (!res.succeeded()) {
        logger.error("ERROR Update failed:", res.cause());
      }
    });
  }

  private final Logger logger = LoggerFactory.getLogger(WriterDataProcessor.class.getName());
}
