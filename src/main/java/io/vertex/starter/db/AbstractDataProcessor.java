package io.vertex.starter.db;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public abstract class AbstractDataProcessor implements Handler<Message<String>> {
  public final String SQL;

  public final SQLClient sqlClient;

  public AbstractDataProcessor(SQLClient sqlClient, String sql) {
    this.sqlClient = sqlClient;
    this.SQL = sql;
  }

  public abstract void processData(SQLConnection connection, String jsonString);

  public void process(String jsonString) {
    sqlClient.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection connection = res.result();

        logger.info("call processData");
        processData(connection, jsonString);

        connection.close();
      } else {
        logger.error("ERROR got no connection", res.cause());
      }
    });
  }

  @Override
  public void handle(Message<String> event) {
    logger.info(AbstractDataProcessor.class.getName() + " handle");
    process(event.body());
  }

  private final Logger logger = LoggerFactory.getLogger(AbstractDataProcessor.class.getName());

}
