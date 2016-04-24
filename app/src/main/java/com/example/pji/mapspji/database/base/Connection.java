package com.example.pji.mapspji.database.base;

import java.sql.SQLException;

public class Connection {
	protected Requete requete;

	public Connection() throws SQLException, ClassNotFoundException {
		requete=new Requete();
	}

}