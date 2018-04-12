package io.vertx.starter;

import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class DataLister<T extends Data> extends AbstractDataProcessor<T> {
  private ListerStreamHandler listerStreamHandler;

  public DataLister(SQLClient sqlClient, ListerStreamHandler listerStreamHandler) {
    super(sqlClient, "select msg from data");
    this.listerStreamHandler = listerStreamHandler;
  }

  @Override
  public void processData(SQLConnection connection, T data) {
    JsonArray params = new JsonArray();

    connection.queryStreamWithParams(SQL, params, listerStreamHandler);

  }

  private final Logger logger = LoggerFactory.getLogger(DataLister.class.getName());
}
