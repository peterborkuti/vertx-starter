package io.vertex.starter.common.data;

import io.vertx.core.json.JsonObject;

public interface DataProvider {
  public Data provide(JsonObject parameters);
}
