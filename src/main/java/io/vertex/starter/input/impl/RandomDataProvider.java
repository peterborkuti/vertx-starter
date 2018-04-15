package io.vertex.starter.input.impl;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.data.DataProvider;
import io.vertx.core.json.JsonObject;

import java.util.Random;

public class RandomDataProvider implements DataProvider {
  private Random rnd = new Random();


  @Override
  public Data provide(JsonObject parameters) {
    return new Data(rnd.nextInt());
  }
}
