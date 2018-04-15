package io.vertex.starter.input.impl;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.data.DataProvider;
import io.vertx.core.json.JsonObject;

import java.util.Random;

public class CounterDataProvider implements DataProvider {
  private int i = 0;


  @Override
  public Data provide(JsonObject parameters) {
    if ( i == Integer.MAX_VALUE) {
      i = 0;
    }

    return new Data(i++);
  }
}
