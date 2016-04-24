package com.example.pji.mapspji.api.dom.src.dom;


import com.example.pji.mapspji.api.dom.src.Exception.UserNameOrPasswordException;
import com.example.pji.mapspji.api.dom.src.user.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class DomConnection {
	final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	protected DocumentBuilder builder = null;
	protected Document document = null;
	protected Element racine = null;
	protected ArrayList<User> users = null;
	protected File filexml;

	public DomConnection(File filexml) throws ParserConfigurationException, SAXException, IOException {
		this.filexml = filexml;
		builder = factory.newDocumentBuilder();
		document = builder.parse(filexml);
		racine = document.getDocumentElement();
		// On recupere tous les Users
		users = this.getAllUser();
	}

	// Recuperer toutes les informations des Utilisateurs
	public ArrayList<User> getAllUser() {
		ArrayList<User> users = new ArrayList<User>();
		NodeList enfants = racine.getChildNodes();
		int nbenfants = enfants.getLength();
		for (int i = 0; i < nbenfants; i++) {
			Node enfant = enfants.item(i);
			if (enfant.getNodeType() == Node.ELEMENT_NODE && enfant.getNodeName().equals("compte")) {
				users.add(getUser((Element) enfant));
			}
		}
		return users;
	}

	// Transforme une balise Compte en une instance User
	public User getUser(Element user) {
		Element nom = (Element) user.getElementsByTagName("user").item(0);
		Element pass = (Element) user.getElementsByTagName("pass").item(0);
		return new User(nom.getTextContent(), pass.getTextContent(),user);
	}

	// Demande la connection d'un utilisateur et renvoie sa balise specifique
	public Element connection(String username, String userpass) throws UserNameOrPasswordException, ParserConfigurationException, SAXException, IOException {
		int taille = users.size();
		User user;
		for (int i = 0; i < taille; i++) {
			user = this.users.get(i);
			String name = user.getUsername();
			String pass = user.getPass();
			Element element=user.getElement();
			if (name.equals(username) && pass.equals(userpass)) {
				return element;
			}
		}
		throw new UserNameOrPasswordException();
	}
	public void updateFile() throws TransformerFactoryConfigurationError, TransformerException, IOException {
		Transformer tf = TransformerFactory.newInstance().newTransformer();
		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		tf.setOutputProperty(OutputKeys.INDENT, "yes");
		tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		tf.transform(new DOMSource(document), new StreamResult(filexml));

	}
}
