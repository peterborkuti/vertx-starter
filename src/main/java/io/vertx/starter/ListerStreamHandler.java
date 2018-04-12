package io.vertx.starter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLRowStream;

public class ListerStreamHandler<T> implements Handler<AsyncResult<SQLRowStream>> {
  private void processResult(SQLRowStream sqlRowStream) {
    sqlRowStream
      .resultSetClosedHandler(v -> {
        logger.debug("resultSetClosedHandler");
        // will ask to restart the stream with the new result set if any
        //sqlRowStream.moreResults();
      })

      .handler(row -> {
        System.out.println(row.getString(0));
      })

      .endHandler(v -> {
        logger.debug("Stream ends");
      });
  }


  @Override
  public void handle(AsyncResult<SQLRowStream> stream) {
    if (stream.succeeded()) {
      SQLRowStream sqlRowStream = stream.result();

      processResult(sqlRowStream);
    }
    else {
      logger.error("ERROR with stream", stream.cause());
    }
  }

  private final Logger logger = LoggerFactory.getLogger(ListerStreamHandler.class.getName());

}
