package io.vertx.starter;

import io.vertex.starter.common.Utils;
import io.vertex.starter.common.data.Data;
import io.vertex.starter.common.data.DataProvider;
import io.vertex.starter.db.DataProcessorVerticle;
import io.vertex.starter.db.impl.ReaderDataProcessor;
import io.vertex.starter.db.impl.WriterDataProcessor;
import io.vertex.starter.input.impl.ConstantDataProvider;
import io.vertex.starter.input.PeriodicPublisherVerticle;
import io.vertex.starter.input.impl.CounterDataProvider;
import io.vertex.starter.common.streamhandler.DBReaderStreamHandler;
import io.vertex.starter.output.log.LoggerSQLRowStreamHandler;
import io.vertex.starter.output.rest.WebServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLClient;

import java.util.ArrayList;
import java.util.List;

public class MainVerticle extends AbstractVerticle {

	private List<String> vertexId = new ArrayList<String>();
  private SQLClient sqlWriterClient;
  private SQLClient sqlReaderClient;

	@Override
	public void start(Future<Void> startFuture) {
		System.out.println("MainVerticle started!");

    JsonObject mySQLClientConfig = Utils.getDBConfig(config());

    Utils.createTableIfNotExists(vertx, mySQLClientConfig, Utils.getSQLConfig(config()));

    createDBReaderVerticle(mySQLClientConfig);
    createDBWriterVerticle(mySQLClientConfig);
    triggerDBReader();
    triggerDBWriter();

    vertx.deployVerticle(
      new WebServerVerticle(
        Utils.getHttpConfig(config()).getInteger("port"),
        config().getJsonObject("rest"),
        Utils.getSQLConfig(config())
      )
    );
	}


  private void createDBReaderVerticle(JsonObject sqlClientConfig) {
    sqlReaderClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    DBReaderStreamHandler streamHandler = new DBReaderStreamHandler(new LoggerSQLRowStreamHandler());

    ReaderDataProcessor rdp =
      new ReaderDataProcessor(
        sqlReaderClient,
        streamHandler,
        Utils.getSQLConfig(config()).getString("listdata"));

    vertx.deployVerticle(new DataProcessorVerticle(rdp, "triggerReader"));
  }

  private void createDBWriterVerticle(JsonObject sqlClientConfig) {
    sqlWriterClient = MySQLClient.createNonShared(vertx, sqlClientConfig);

    WriterDataProcessor wdp =
      new WriterDataProcessor(sqlWriterClient, Utils.getSQLConfig(config()).getString("insertdata"));

    vertx.deployVerticle(new DataProcessorVerticle(wdp, "triggerWriter"));
  }

  private void triggerDBWriter() {
    DataProvider dp = new CounterDataProvider();

    vertx.deployVerticle(new PeriodicPublisherVerticle("triggerWriter", dp, 2000));
  }

  private void triggerDBReader() {
    DataProvider dp = new ConstantDataProvider(new Data(1));

    vertx.deployVerticle(new PeriodicPublisherVerticle("triggerReader", dp, 5000));
  }

	@Override
	public void stop(Future stopFuture) throws Exception {
		System.out.println("MainVerticle stopped!");

		sqlWriterClient.close();
		sqlReaderClient.close();

		for (String id: vertexId) {
      vertx.undeploy(id);
    }
	}

  private final Logger logger = LoggerFactory.getLogger(MainVerticle.class.getName());

}
