package com.example.pji.mapspji.api.dom.src.user;

import org.w3c.dom.Element;

public class User {
	private String username;
	private String pass;
	private Element element;
	private Position position;
	private String id;
	//Element est le contenue de la balise qui contient le compte (username,pass)
	public User(String username,String pass,Element element){
		this.username=username;
		this.pass=pass;
		this.element=element;
		this.position=null;
	}
	public User(String username,String id,Position p){
		this.username=username;
		this.pass=null;
		this.element=null;
		this.position=p;
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public Element getElement() {
		return element;
	}
	public String getUsername() {
		return username;
	}
	public String getPass() {
		return pass;
	}
	public String toString(){
		if (pass!=null)
		return "User:"+username+" "+"Pass:"+" "+pass;
		else
		return "User:"+username+"\n"+position.toString();
	}
}
