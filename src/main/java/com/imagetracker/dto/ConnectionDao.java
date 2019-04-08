package com.imagetracker.dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.imagetracker.bean.File;

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

	public int getUserList(Connection conn, String username, String password) {
		if (conn == null) {
			conn = RetriveConnection();
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select username,password,userId from userDB.userInfo ");
			while (rs.next()) {
				if (rs.getString("username").equals(username) && rs.getString("password").equals(password)) {
					return rs.getInt("userId");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// con.close();
		return 0;
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
	
	public boolean insertUploadedImageInfo(Connection conn,int userId, String username, String imageName,String url,String lables) {
		if (conn == null) {
			conn = RetriveConnection();
		}
		try {
			String insertTableSQL = "INSERT INTO userDB.imagesInfo" + "(userId,username,imageName,url,lables) VALUES" + "(?,?,?,?,?)";

			PreparedStatement preparedStatement = conn.prepareStatement(insertTableSQL);
			preparedStatement.setInt(1, userId);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, imageName);
			preparedStatement.setString(4, url);
			preparedStatement.setString(5, lables);
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into userDB.imagesInfo table!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// con.close();
		return false;
	}
	
	public List<File> getUsersImageInfo(Connection conn, String username,String userId) {
		List<File> fileList = new ArrayList<File>();
		if (conn == null) {
			conn = RetriveConnection();
		}
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select s3Url,lables from userDB.userInfo where username="+username+ " and userId="+userId);
			while (rs.next()) {
				File file = new File();
				file.setUrl(rs.getString("s3Url"));
				file.setLabels(rs.getString("lables"));
				fileList.add(file);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// con.close();
		return fileList;
	}

}
