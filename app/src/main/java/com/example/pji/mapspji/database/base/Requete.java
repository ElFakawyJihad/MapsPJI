package com.example.pji.mapspji.database.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Requete {
	private static final String url = "http://maplille1.alwaysdata.net/";

	public String reponse(String demande) throws IOException {
		URL oracle = new URL(url + demande);
		BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
		String resultRequete = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			resultRequete = resultRequete + inputLine;
		in.close();
		return resultRequete;
	}
}
