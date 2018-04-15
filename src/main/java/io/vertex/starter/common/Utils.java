package io.vertex.starter.common;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

public class Utils {
  public static void createTableIfNotExists(final Vertx vertx, JsonObject sqlClientConfig, JsonObject sql) {
    final Logger logger = LoggerFactory.getLogger(Utils.class.getName());

    SQLClient sqlClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    sqlClient.getConnection(
      event -> {
        if (event.succeeded()) {
          event.result().query(sql.getString("checktable"), event1 -> {
            if (event1.failed()) {
              logger.info("trying to create table data... ");

              event.result().execute(sql.getString("createtable"), event2 -> {
                if (event2.succeeded()) {
                  logger.info("table created.");
                }
                {
                  logger.error("ERROR: table my not exists." + event2.cause());
                }
              });
            }
          });
        } else {
          logger.error("ERROR: bad connection parameters or there is no database!" + event.cause());
        }
      }

    );

    sqlClient.close();
  }

  public static JsonObject getDBConfig(JsonObject config) {
    JsonObject dbConfig = config.getJsonObject("db");

    if (dbConfig == null || dbConfig.isEmpty()) {
      dbConfig = new JsonObject()
        .put("username", "root")
        .put("database", "vertx")
        .put("password", "root");
    }

    return dbConfig;
  }

  public static JsonObject getSQLConfig(JsonObject config) {
    JsonObject sqlConfig = config.getJsonObject("sql");

    return sqlConfig;
  }

  public static JsonObject getHttpConfig(JsonObject config) {
    JsonObject sqlConfig = config.getJsonObject("http");

    return sqlConfig;
  }
}
