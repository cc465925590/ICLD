package cc.icld.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class AccessConnet {
	public Connection con = null;

	// public PreparedStatement stmt = null;

	public void connect() {
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:PPDP";
			con = DriverManager.getConnection(url, "", "");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void close() {

		try {
			// stmt.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String sql = "select race,count(race) from Adult_o group by race";
		System.out.println(sql);
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:PPDP";
			Connection con = DriverManager.getConnection(url, "", "");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			for (int i = 1; i <= data.getColumnCount(); i++) {
				String columnName = data.getColumnName(i);
				System.out.print(columnName + '\t');
			}
			System.out.println();
			int i = 0;
			while (rs.next() && i < 10) {
				i++;
				System.out.print(rs.getString(1) + '\t');
				System.out.print(rs.getString(2) + '\t');
				/*
				 * System.out.print(rs.getString(3) + '\t');
				 * System.out.print(rs.getString(4) + '\t');
				 * System.out.print(rs.getString(5) + '\t');
				 */
				System.out.println();
			}
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
