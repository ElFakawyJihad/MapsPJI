package com.example.pji.mapspji.database.groupe;

import com.example.pji.mapspji.database.base.Connection;
import com.example.pji.mapspji.database.exception.UserHaveNotGroupException;
import com.example.pji.mapspji.database.exception.UserIsInGroupException;
import com.example.pji.mapspji.database.localisation.Position;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class GroupUser extends Connection {

	public GroupUser() throws SQLException, ClassNotFoundException {
		super();
	}

	public HashMap<String, Position> getAllPosition(int userid) throws IOException, UserHaveNotGroupException {
		HashMap<String, Position> map = new HashMap<String, Position>();
		String page = "userGroup.php";
		String user = "?user=" + userid;
		String reponse = this.requete.reponse(page + user);
		Document doc = Jsoup.parse(reponse);
		if (doc.getElementById("probleme") != null) {
			throw new UserHaveNotGroupException();
		}
		Elements personnes = doc.getElementsByTag("personne");
		int size = personnes.size();
		for (int i = 0; i < size; i++) {
			String username = personnes.get(i).getElementsByTag("user").text();
			Double longitude = Double.parseDouble(personnes.get(i).getElementsByTag("longitude").text());
			Double latitude = Double.parseDouble(personnes.get(i).getElementsByTag("latitude").text());
			map.put(username, new Position(longitude, latitude));
		}
		return map;
	}

	public void addInGroupe(int userid, int groupid) throws IOException, UserIsInGroupException {
		String page = "addUsersGroup.php";
		String user = "?user=" + userid;
		String groupe = "&groupe=" + groupid;
		String reponse = this.requete.reponse(page + user + groupe);
		Document doc = Jsoup.parse(reponse);
		if (doc.getElementById("probleme") != null) {
			throw new UserIsInGroupException();
		}
	}

	public void deleteInGroupe(int userid, int groupid) throws IOException {
		String page = "deleteUserGroup.php";
		String user = "?user=" + userid;
		String groupe = "&groupe=" + groupid;
		this.requete.reponse(page + user + groupe);
	}
	/**
	 * Les groupes possibles pour l'utilisateur pour s'abonner
	 *
	 * @param userid
	 *            id de l'utilsateur en question
	 * @return
	 * @throws IOException
	 */
	public HashMap<String, Integer> getOtherGroup(int userid) throws IOException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String page = "otherGroup.php";
		String user = "?user=" + userid;
		String reponse = this.requete.reponse(page + user);
		Document doc = Jsoup.parse(reponse);
		Elements element = doc.getElementsByTag("groupe");
		int size = element.size();
		for (int i = 0; i < size; i++) {
			int id = Integer.parseInt(element.get(i).getElementsByTag("id").text());
			String name = element.get(i).getElementsByTag("name").text();
			map.put(name, id);
		}
		return map;
	}
	/**
	 *
	 * @param userid id de l'utilisateur
	 * @return liste de mes groupes
	 * @throws IOException
	 */
	public HashMap<String,Integer> getMyGroup(int userid) throws IOException{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String page = "mygroups.php";
		String user = "?user=" + userid;
		String reponse = this.requete.reponse(page + user);
		Document doc = Jsoup.parse(reponse);
		Elements element = doc.getElementsByTag("groupe");
		int size = element.size();
		for (int i = 0; i < size; i++) {
			int id = Integer.parseInt(element.get(i).getElementsByTag("id").text());
			String name = element.get(i).getElementsByTag("name").text();
			map.put(name, id);
		}
		return map;
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException, UserHaveNotGroupException,
			SQLException, UserIsInGroupException {
		new GroupUser().addInGroupe(17, 1);
	}
}
