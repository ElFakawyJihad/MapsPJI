package com.example.pji.mapspji.database.exception;

public class PositionIsNotAvailableException extends Exception{
	public PositionIsNotAvailableException(){
		System.out.println("L'utilisateur ne contient pas de position");
	}
}
