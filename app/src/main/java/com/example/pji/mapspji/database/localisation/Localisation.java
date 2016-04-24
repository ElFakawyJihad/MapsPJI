package com.example.pji.mapspji.database.localisation;

import com.example.pji.mapspji.database.base.Connection;
import com.example.pji.mapspji.database.exception.PositionIsNotAvailableException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLException;

public class Localisation extends Connection{
	private int user;
	public Localisation(int user) throws SQLException, ClassNotFoundException {
		super();
		this.user=user;
	}
	public void setLocalisation(Position position) throws IOException{
		String page = "addLocalisation.php";
		String user = "?user=" + this.user;
		String latitude = "&latitude=" +position.getLatitude();
		String longitude = "&longitude=" +position.getLongitude();
		//Mise a jour de la position
		this.requete.reponse(page + user +latitude+longitude);
	}
	public Position getLocalisation() throws IOException, PositionIsNotAvailableException{
		String page = "localisationUser.php";
		String user = "?user=" + this.user;
		String reponse = this.requete.reponse(page+user);
		Document doc = Jsoup.parse(reponse);
		if (doc.getElementById("probleme") != null){
			throw new PositionIsNotAvailableException();
		}
		double longitude=Double.parseDouble(doc.getElementById("longitude").text());
		double latitude=Double.parseDouble(doc.getElementById("latitude").text());
		return new Position(longitude,latitude);
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, PositionIsNotAvailableException {
		System.out.println(new Localisation(12).getLocalisation());	
	}

}
