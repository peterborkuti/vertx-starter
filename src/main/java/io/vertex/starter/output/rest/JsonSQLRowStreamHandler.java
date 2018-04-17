package io.vertex.starter.output.rest;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.streamhandler.SQLRowStreamHandler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

public class JsonSQLRowStreamHandler implements SQLRowStreamHandler {
  private List<Data> data = new ArrayList<>();
  private HttpServerResponse response;

  private JsonSQLRowStreamHandler() {}

  public  JsonSQLRowStreamHandler(HttpServerResponse response) {
    this.response = response;
  }

  @Override
  public String endHandler() {
    String  json = Json.encodePrettily(data);

    response.putHeader("content-type", "application/json; charset=utf-8");
    response.end(json);

    data.clear();

    return json;
  }

  /**
   * Something has happened, so handle it.
   *
   * @param event the event to handle
   */
  @Override
  public void handle(JsonArray event) {
    data.add(Data.createFromJsonArray(event));
  }
}
