package io.vertex.starter.output.rest;

import io.vertex.starter.common.Utils;
import io.vertex.starter.common.streamhandler.DBReaderStreamHandler;
import io.vertex.starter.db.impl.ReaderDataProcessor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;

public class WebServerVerticle extends AbstractVerticle {
  public final int PORT;
  public final JsonObject restConfig;
  public final JsonObject sqlConfig;
  public final String API;

  public WebServerVerticle(int port, JsonObject restConfig, JsonObject sqlConfig) {
    this.restConfig = restConfig;
    this.sqlConfig = sqlConfig;
    this.API = "/" + restConfig.getString("api") + "/";
    this.PORT = port;
  }

  @Override
  public void start(Future<Void> future) {
    Router router = Router.router(vertx);

    router.get(API + restConfig.getString("all")).handler(this::restAllRouter);
    router.get(API + restConfig.getString("last")).handler(this::restLastRouter);
    router.get(API + restConfig.getString("list")).handler(this::restListRouter);

    createHttpServer(future, router);

    logger.info("WebserverVerticle started");
  }

  private void restAllRouter(RoutingContext routingContext) {
    JsonObject sqlClientConfig = Utils.getDBConfig(config());
    SQLClient sqlReaderClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    DBReaderStreamHandler streamHandler = new DBReaderStreamHandler(new JsonSQLRowStreamHandler(routingContext.response()));

    ReaderDataProcessor rdp =
      new ReaderDataProcessor(
        sqlReaderClient,
        streamHandler,
        sqlConfig.getString("listdata"));

    rdp.process("");
  }

  private void restLastRouter(RoutingContext routingContext) {
    logger.info("restLastRouter started");
    JsonObject sqlClientConfig = Utils.getDBConfig(config());
    SQLClient sqlReaderClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    DBReaderStreamHandler streamHandler = new DBReaderStreamHandler(new JsonSQLRowStreamHandler(routingContext.response()));

    ReaderDataProcessor rdp =
      new ReaderDataProcessor(
        sqlReaderClient,
        streamHandler,
        sqlConfig.getString("lastdata"));

    logger.info("restLastRouter starts processing");
    rdp.process("");
    logger.info("restLastRouter ended");
  }

  private void restListRouter(RoutingContext routingContext) {
    int from = getPathParam(routingContext,"from", 0);
    int to = getPathParam(routingContext,"to", 0);

    JsonArray params = new JsonArray().add(from).add(to);
    JsonObject sqlClientConfig = Utils.getDBConfig(config());
    SQLClient sqlReaderClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    DBReaderStreamHandler streamHandler = new DBReaderStreamHandler(new JsonSQLRowStreamHandler(routingContext.response()));

    ReaderDataProcessor rdp =
      new ReaderDataProcessor(
        sqlReaderClient,
        streamHandler,
        sqlConfig.getString("listfromto"));

    rdp.process(params.encode());
  }

  private int getPathParam(RoutingContext context, String paramName, int defaultValue) {
    String value = context.pathParam(paramName);
    Integer integerValue = null;

    try {
      integerValue = Integer.valueOf(value);
    } catch (NumberFormatException e){
    }

    return integerValue == null ? defaultValue : integerValue;
  }

  private void createHttpServer(Future<Void> fut, Router router) {
    vertx
      .createHttpServer()
      .requestHandler(router::accept)
      .listen(
        PORT,
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
