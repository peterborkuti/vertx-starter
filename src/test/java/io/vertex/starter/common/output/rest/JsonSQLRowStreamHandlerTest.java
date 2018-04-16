package io.vertex.starter.common.output.rest;

import io.vertex.starter.output.rest.JsonSQLRowStreamHandler;
import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;

import io.vertx.ext.web.RoutingContext;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;



public class JsonSQLRowStreamHandlerTest {
  @Test
  public void testEndhandler() {
      abstract class MyRoutingContext implements RoutingContext {
        @Override
        public HttpServerResponse response() {
          return mock(HttpServerResponse.class, RETURNS_DEEP_STUBS);
        }

      }

      RoutingContext rc = mock(MyRoutingContext.class, RETURNS_DEEP_STUBS);

      JsonSQLRowStreamHandler jsh = new JsonSQLRowStreamHandler(rc);

      jsh.handle(new JsonArray().add(1));
      jsh.endHandler();
/*
      verify(rc)
        .response()
        .end("hello");
*/
  }
}
