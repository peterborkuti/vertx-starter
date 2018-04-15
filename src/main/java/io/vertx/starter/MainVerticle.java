package io.vertx.starter;

import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.data.DataProvider;
import io.vertex.starter.db.DataProcessorVerticle;
import io.vertex.starter.db.impl.ReaderDataProcessor;
import io.vertex.starter.db.impl.WriterDataProcessor;
import io.vertex.starter.input.impl.ConstantDataProvider;
import io.vertex.starter.input.PeriodicPublisherVerticle;
import io.vertex.starter.input.impl.RandomDataProvider;
import io.vertex.starter.common.streamhandler.DBReaderStreamHandler;
import io.vertex.starter.output.LoggerSQLRowStreamHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

	private List<String> vertexId = new ArrayList<String>();
  private SQLClient sqlWriterClient;
  private SQLClient sqlReaderClient;

  private void createDBReaderVerticle(JsonObject sqlClientConfig) {
    sqlReaderClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    DBReaderStreamHandler streamHandler = new DBReaderStreamHandler(new LoggerSQLRowStreamHandler());

    ReaderDataProcessor rdp = new ReaderDataProcessor(sqlReaderClient, streamHandler);

    vertx.deployVerticle(new DataProcessorVerticle(rdp, "triggerReader"));
  }

  private void createDBWriterVerticle(JsonObject sqlClientConfig) {
    sqlWriterClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    WriterDataProcessor wdp = new WriterDataProcessor(sqlWriterClient);

    vertx.deployVerticle(new DataProcessorVerticle(wdp, "triggerWriter"));
  }

  private void triggerDBWriter() {
    DataProvider dp = new RandomDataProvider();

    vertx.deployVerticle(new PeriodicPublisherVerticle("triggerWriter", dp, 1000));
  }

  private void triggerDBReader() {
    DataProvider dp = new ConstantDataProvider(new Data(1));

    vertx.deployVerticle(new PeriodicPublisherVerticle("triggerReader", dp, 5000));
  }

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("MainVerticle started!");

    JsonObject mySQLClientConfig = new JsonObject()
      .put("username", "root")
      .put("database", "vertx")
      .put("password", "root");

    createDBReaderVerticle(mySQLClientConfig);
    createDBWriterVerticle(mySQLClientConfig);
    triggerDBReader();
    triggerDBWriter();
	}

	@Override
	public void stop(Future stopFuture) throws Exception {
		System.out.println("MyVerticle stopped!");

		sqlWriterClient.close();
		sqlReaderClient.close();

		for (String id: vertexId) {
      vertx.undeploy(id);
    }
	}

}
