package io.vertex.starter.common.data;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataTest {
  @Test
  public void testInstantiation() {
    assertEquals(1, new Data(1).data);
  }

  @Test
  public void testCreation() {
    assertEquals(1, Data.createFromJsonArray(new JsonArray().add(1)).data);
    assertEquals(1, Data.createFromJsonObject(new JsonObject().put("data", 1)).data);
    assertEquals(1, Data.createFromJsonString(new JsonObject().put("data", 1).encode()).data);
  }

  @Test
  public void testTo() {
    JsonArray arr = new JsonArray().add(1);

    assertEquals(arr.encode(), new Data(1).toJsonArray().encode());

    JsonObject jo = new JsonObject().put("data", 1);

    assertEquals(jo.encode(), new Data(1).toJsonObject().encode());

    assertEquals(jo.encode(), new Data(1).toJsonString());

    assertEquals(jo.encodePrettily(), new Data(1).toString());
  }
}
