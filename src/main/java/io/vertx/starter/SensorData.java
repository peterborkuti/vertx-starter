package io.vertx.starter;

import io.vertx.core.json.JsonArray;

public class SensorData implements Data {
  private int data;

  public SensorData(int data) {
    this.data = data;
  }

  public static SensorData createFromJsonArray(JsonArray ja) {
    return new SensorData(ja.getInteger(0));
  }

  public int getData() {
    return data;
  }

  public void setData(int data) {
    this.data = data;
  }

  public String toString() {
    return String.valueOf(data);
  }

  public JsonArray toJsonArray() {
    return new JsonArray().add(data);
  }
}
