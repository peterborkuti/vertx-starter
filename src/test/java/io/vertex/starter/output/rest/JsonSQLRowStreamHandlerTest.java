package io.vertex.starter.output.rest;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.output.rest.JsonSQLRowStreamHandler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class JsonSQLRowStreamHandlerTest {

  @Test
  public void testHandlerBehaviourAndEndhandlerOutput() {
      HttpServerResponse response = mock(HttpServerResponse.class);

      JsonSQLRowStreamHandler jsh = new JsonSQLRowStreamHandler(response);

      List<Data> test = new ArrayList<>();
      test.add(new Data(1));

      for (Data d: test) {
        jsh.handle(d.toJsonArray());
      }

      jsh.endHandler();

      verify(response).putHeader(eq("content-type"), eq("application/json; charset=utf-8"));
      verify(response).end(eq(Json.encodePrettily(test)));
  }
}
