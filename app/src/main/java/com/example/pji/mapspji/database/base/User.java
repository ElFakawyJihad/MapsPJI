package com.example.pji.mapspji.database.base;

import com.example.pji.mapspji.database.exception.NameIsUseException;
import com.example.pji.mapspji.database.exception.UserOrPasswordFalseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLException;

public class User extends Connection {

	public User() throws SQLException, ClassNotFoundException {
		super();
	}

	/**
	 *
	 * @param username
	 * @param password
	 * @return -1 if it's not exist
	 * @throws SQLException
	 * @throws IOException
	 * @throws UserOrPasswordFalseException
	 */
	public int identification(String username, String password) throws SQLException, IOException, UserOrPasswordFalseException {
		String page = "users.php";
		String user = "?username=" + username;
		String pass = "&password=" + password;
		String reponse = this.requete.reponse(page + user + pass);
		Document doc = Jsoup.parse(reponse);
		int id= Integer.parseInt(doc.getElementById("id").text());
		if (id==-1){
			throw new UserOrPasswordFalseException();
		}
		return id;
	}

	/*
	 * public boolean verifUserExist(String username) throws SQLException {
	 * java.sql.PreparedStatement requete = super.connect .prepareStatement(
	 * "select iduser from users where username=?"); requete.setString(1,
	 * username); ResultSet result = requete.executeQuery(); return
	 * result.next(); } /**
	 *
	 * @param username
	 *
	 * @param password
	 *
	 * @return
	 *
	 * @throws SQLException
	 */
	public int createUser(String username, String password) throws SQLException, IOException, NameIsUseException {
		String page = "usersAdd.php";
		String user = "?username=" + username;
		String pass = "&password=" + password;
		String reponse = this.requete.reponse(page + user + pass);
		Document doc = Jsoup.parse(reponse);
		if (doc.getElementById("probleme") != null) {
			throw new NameIsUseException();
		}
		return Integer.parseInt(doc.getElementById("id").text());
	}
}