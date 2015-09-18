package test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyDao {
	public String readLine(int id) throws Exception {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:h2:tcp://localhost/~/test", "sa", "");
		PreparedStatement ps = conn
				.prepareStatement("SELECT * FROM MY_TABLE WHERE ID = ?;");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		rs.first();
		String result = rs.getString("NAME");
		conn.close();
		return result;
	}
}
