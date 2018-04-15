package io.vertex.starter.output;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.streamhandler.SQLRowStreamHandler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class LoggerSQLRowStreamHandler implements SQLRowStreamHandler {
  /**
   * Something has happened, so handle it.
   *
   * @param event the event to handle
   */
  @Override
  public void handle(JsonArray event) {
    logger.info(Data.createFromJsonArray(event).toJsonString());
  }

  private final Logger logger = LoggerFactory.getLogger(LoggerSQLRowStreamHandler.class.getName());
}
