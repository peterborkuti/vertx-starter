package io.vertex.starter.common.streamhandler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.SQLRowStream;

public class DBReaderStreamHandler<T> implements Handler<AsyncResult<SQLRowStream>> {
  private SQLRowStreamHandler rowStreamHandler;

  private DBReaderStreamHandler() {}

  public DBReaderStreamHandler(SQLRowStreamHandler rowStreamHandler) {
    this.rowStreamHandler = rowStreamHandler;
  }

  private void processResult(SQLRowStream sqlRowStream) {
    sqlRowStream
      .resultSetClosedHandler(v -> {
        logger.debug("resultSetClosedHandler");
        // will ask to restart the stream with the new result set if any
        //sqlRowStream.moreResults();
      })

      .handler(rowStreamHandler)

      .endHandler(v -> rowStreamHandler.endHandler());
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

  private final Logger logger = LoggerFactory.getLogger(DBReaderStreamHandler.class.getName());

}
