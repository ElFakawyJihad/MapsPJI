package com.example.pji.mapspji.database.groupe;

import com.example.pji.mapspji.database.base.Connection;
import com.example.pji.mapspji.database.exception.GroupNameIsUseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Group extends Connection{

	public Group() throws SQLException, ClassNotFoundException {
		super();
	}
	public HashMap<String,Integer> getAllGroup() throws ClassNotFoundException, IOException, SQLException{
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		Document doc = Jsoup.parse(new Group().requete.reponse("allGroup.php"));
		Elements element=doc.getElementsByTag("groupe");
		int size=element.size();
		for (int i=0;i<size;i++){
			int id=Integer.parseInt(element.get(i).getElementsByTag("id").text());
			String name=element.get(i).getElementsByTag("name").text();
			map.put(name, id);
		}
		return map;
	}
	public void addNewGroup(String namegroup) throws IOException, GroupNameIsUseException{
		String page = "addGroup.php";
		String name = "?name=" + namegroup;
		String reponse = this.requete.reponse(page+name);
		Document doc = Jsoup.parse(reponse);
		if (doc.getElementById("probleme") != null){
			throw new GroupNameIsUseException();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException, GroupNameIsUseException {
		new Group().addNewGroup("soccerrr");
	}

}
