package io.vertex.starter.output.rest;

import io.vertex.starter.common.Utils;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class WebServerVerticleTest {

  private Vertx vertx;
  private String verticleId;
  private JsonObject config;
  private int PORT;

  @Before
  public void setUp(TestContext tc) {
    vertx = Vertx.vertx();

    setConfigFromFile();
    PORT = Utils.getHttpConfig(config).getInteger("port");

    WebServerVerticle wsv = new WebServerVerticle(PORT, Utils.getRESTConfig(config), Utils.getDBConfig(config));
    vertx.deployVerticle(wsv,
      s -> {
        verticleId = s.result();
        tc.asyncAssertSuccess();
      });
  }

  @After
  public void tearDown(TestContext tc) {
    vertx.close(tc.asyncAssertSuccess());
  }

  @Test
  public void testThatTheServerIsStarted(TestContext tc) {
    Async async = tc.async();
    vertx.
      createHttpClient().getNow(PORT, "localhost", "/", response -> {
      tc.assertEquals(response.statusCode(), 200);
      response.bodyHandler(body -> {
        tc.assertTrue(body.length() > 0);
        async.complete();
      });
    });

  }

  private void setConfigFromResult(AsyncResult<JsonObject> config) {
    this.config = config.succeeded() ? config.result() : null;
  }

  private void setConfigFromFile() {
    ConfigStoreOptions file = new ConfigStoreOptions()
      .setType("file")
      .setFormat("json")
      .setConfig(new JsonObject().put("path", "./testconf.json"));

    ConfigRetrieverOptions options = new ConfigRetrieverOptions()
      .addStore(file);

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    retriever.getConfig(this::setConfigFromResult);
  }
}
