package io.vertex.starter.db.impl;

import io.vertex.starter.db.AbstractDataProcessor;
import io.vertex.starter.common.streamhandler.DBReaderStreamHandler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public class ReaderDataProcessor extends AbstractDataProcessor {
  private DBReaderStreamHandler streamHandler;

  public ReaderDataProcessor(SQLClient sqlClient, DBReaderStreamHandler streamHandler, String listData) {
    super(sqlClient, listData);
    this.streamHandler = streamHandler;
  }

  @Override
  public void processData(SQLConnection connection, String jsonString) {
    if ("".equals(jsonString)) jsonString = "[]";
    JsonArray params = new JsonArray(jsonString);

    connection.queryStreamWithParams(SQL, params, streamHandler);

  }

  private final Logger logger = LoggerFactory.getLogger(ReaderDataProcessor.class.getName());
}
