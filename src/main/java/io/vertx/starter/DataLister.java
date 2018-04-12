package io.vertx.starter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;

public class DataLister<T> extends AbstractDataProcessor<T> {
  public DataLister(SQLClient sqlClient) {
    super(sqlClient, "select msg from data");
  }

  @Override
  public void processData(SQLConnection connection, T data) {
    JsonArray params = new JsonArray();

    connection.queryStreamWithParams(SQL, params, new QueryStreamHandler());

  }

  private final Logger logger = LoggerFactory.getLogger(DataLister.class.getName());
}
