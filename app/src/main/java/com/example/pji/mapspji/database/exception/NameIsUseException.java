package com.example.pji.mapspji.database.exception;

public class NameIsUseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NameIsUseException(){
		System.out.println("Nom d'utilisateur deja utilse");
	}
	
}
