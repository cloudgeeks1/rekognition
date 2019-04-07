package com.imagetracker.dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionDao {
	Connection con = null;

	public Connection RetriveConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://user.csad5jjhx9ft.us-east-2.rds.amazonaws.com", "username",
					"password");
		} catch (Exception e) {
			System.out.println(e);
		}
		return con;
	}

	public boolean getUserList(Connection conn, String username, String password) {
		if (conn == null) {
			conn = RetriveConnection();
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select username,password from userDB.userInfo ");
			while (rs.next()) {
				if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
					return true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// con.close();
		return false;
	}
	
	
	public boolean verifyUser(Connection conn, String username) {
		if (conn == null) {
			conn = RetriveConnection();
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select username from userDB.userInfo ");
			while (rs.next()) {
				if (rs.getString("username").equals(username)) {
					return true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// con.close();
		return false;
	}
	

	public boolean createUser(Connection conn, String username, String password) {
		if (conn == null) {
			conn = RetriveConnection();
		}
		try {
			String insertTableSQL = "INSERT INTO userDB.userInfo" + "(username,password) VALUES" + "(?,?)";

			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into userDB.userInfo table!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// con.close();
		return false;
	}

}
