package io.vertex.starter.output.rest;

import io.vertex.starter.common.Utils;
import io.vertex.starter.common.streamhandler.DBReaderStreamHandler;
import io.vertex.starter.db.impl.ReaderDataProcessor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class WebServerVerticle extends AbstractVerticle {
  public final int port;
  public final String SQL;
  public WebServerVerticle(int port, String readerSQl) {
    this.port = port;
    this.SQL = readerSQl;
  }
  @Override
  public void start(Future<Void> future) {
    Router router = Router.router(vertx);

    router.get("/api/getAll").handler(this::getAllRouter);

    createHttpServer(future, router);

    logger.info("WebserverVerticle started");
  }

  private void getAllRouter(RoutingContext routingContext) {
    JsonObject sqlClientConfig = Utils.getDBConfig(config());
    SQLClient sqlReaderClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    DBReaderStreamHandler streamHandler = new DBReaderStreamHandler(new JsonSQLRowStreamHandler(routingContext));

    ReaderDataProcessor rdp =
      new ReaderDataProcessor(
        sqlReaderClient,
        streamHandler,
        SQL);

    rdp.process(null);
  }

  private void createHttpServer(Future<Void> fut, Router router) {
    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        port,
        result -> {
          if (result.succeeded()) {
            fut.complete();
          } else {
            fut.fail(result.cause());
          }
        }
      );
  }

  private final Logger logger = LoggerFactory.getLogger(WebServerVerticle.class.getName());

}
