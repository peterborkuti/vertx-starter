package io.vertx.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;

public class ListerVerticle extends AbstractVerticle {
  public static final String SELECT="select msg from data";

  private DataLister lister;

  public ListerVerticle(DataLister lister) {
    this.lister = lister;
  }


  @Override
  public void start() throws Exception {
    System.out.println("Lister started.");

    lister.process("");
  }

  @Override
  public void stop() throws Exception {
  }
}
