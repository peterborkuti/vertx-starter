package io.vertex.starter.input.impl;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.data.DataProvider;
import io.vertx.core.json.JsonObject;

public class ConstantDataProvider implements DataProvider {
  private Data data;
  public ConstantDataProvider(Data data) {
    this.data = data;
  }


  @Override
  public Data provide(JsonObject parameters) {
    return data;
  }
}
