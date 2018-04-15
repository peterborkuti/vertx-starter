package io.vertex.starter.common.data;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Data {
  public final int data;

  public Data(int data) {
    this.data = data;
  }

  public static Data createFromJsonArray(JsonArray ja) {
    return new Data(ja.getInteger(0));
  }

  public static Data createFromJsonObject(JsonObject jo) {
    return new Data(jo.getInteger("data"));
  }

  public static Data createFromJsonString(String js) {
    return createFromJsonObject(new JsonObject(js));
  }

  public JsonArray toJsonArray() {
    return new JsonArray().add(data);
  }

  public JsonObject toJsonObject() {
    return new JsonObject().put("data", data);
  }

  public String toJsonString() {
    return toJsonObject().encode();
  }

  public String toString() {
    return toJsonObject().encodePrettily();
  }
}
