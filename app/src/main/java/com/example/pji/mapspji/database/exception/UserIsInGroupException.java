package com.example.pji.mapspji.database.exception;

public class UserIsInGroupException extends Exception{
	public UserIsInGroupException(){
		System.out.println("Utilisateur deja present dans le groupe");
	}
}
