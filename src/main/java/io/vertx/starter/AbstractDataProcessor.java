package io.vertx.starter;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;

public abstract class AbstractDataProcessor<T> implements Handler<T> {
  public final String SQL;

  public final SQLClient sqlClient;

  public AbstractDataProcessor(SQLClient sqlClient, String sql) {
    this.sqlClient = sqlClient;
    this.SQL = sql;
  }

  public abstract void processData(SQLConnection connection, T data);

  public void process(T data) {
    sqlClient.getConnection(res -> {
      if (res.succeeded()) {
        SQLConnection connection = res.result();

        logger.info("call processData");
        processData(connection, data);

        connection.close();
      } else {
        logger.error("ERROR got no connection", res.cause());
      }
    });
  }

  @Override
  public void handle(T event) {
    logger.info(AbstractDataProcessor.class.getName() + " handle");
    process(event);
  }

  private final Logger logger = LoggerFactory.getLogger(AbstractDataProcessor.class.getName());

}
