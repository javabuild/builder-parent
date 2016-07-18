package test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.javabuild.Builder;
import net.java.javabuild.Execute;
import net.java.javabuild.Phase;

/**
 * A builder that starts a h2database and creates a schema before tests.
 */
@Builder
public class DatabaseStarter {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(DatabaseStarter.class);
	Server server;

	@Execute(phase = Phase.PROCESS_TEST_CLASSES)
	public void startServer() throws IOException, SQLException,
			ClassNotFoundException {
		server = Server.createTcpServer();
		server.start();
		LOGGER.info("Database started");
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:h2:tcp://localhost/~/test", "sa", "");
		List<String> sqls = FileUtils.readLines(new File(
				"src/test/resources/create_test_database.sql"));
		for (Iterator<String> iterator = sqls.iterator(); iterator.hasNext();) {
			String sql = iterator.next();
			LOGGER.info(sql);
			conn.prepareStatement(sql).executeUpdate();
		}
		conn.close();
	}

	@Execute(phase = Phase.TEST)
	public void stopServer() throws IOException, SQLException {
		server.stop();
		LOGGER.info("Database stopped");
	}

}
