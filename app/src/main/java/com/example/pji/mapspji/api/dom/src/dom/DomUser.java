package com.example.pji.mapspji.api.dom.src.dom;


import com.example.pji.mapspji.api.dom.src.Exception.UserNameOrPasswordException;
import com.example.pji.mapspji.api.dom.src.user.Position;
import com.example.pji.mapspji.api.dom.src.user.User;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




public class DomUser extends DomConnection {
	private String user;
	private String pass;
	private Element element;

	// Constructeur pour Ajouter
	public DomUser(File filexml, String user, String pass) throws ParserConfigurationException, SAXException,
			IOException, UserNameOrPasswordException, TransformerFactoryConfigurationError, TransformerException {
		super(filexml);
		this.user = user;
		this.pass = pass;
		this.connect();
	}

	public void connect() throws UserNameOrPasswordException, ParserConfigurationException, SAXException, IOException,
			TransformerFactoryConfigurationError, TransformerException {
		this.element = this.connection(user, pass);
	}

	public void updatePosition(String longitude,String latitude) throws TransformerFactoryConfigurationError, TransformerException, IOException {
		NodeList enfants = element.getChildNodes();
		int nbenfants = enfants.getLength();
		for (int i = 0; i < nbenfants; i++) {
			Node enfant = enfants.item(i);
			if (enfant.getNodeType()==Node.ELEMENT_NODE && enfant.getNodeName().equals("position")) {
				Node longitudeNode=((Element)enfant).getElementsByTagName("longitude").item(0);
				Node latitudeNode=((Element)enfant).getElementsByTagName("latitude").item(0);
				longitudeNode.setTextContent(longitude);
				latitudeNode.setTextContent(latitude);
			}
		}
		this.updateFile();
	}
	public User getUserById(String id) throws XPathExpressionException{
		//On utilise Xpath pour changer de methode de recuperation des informations
		XPath xPath = XPathFactory.newInstance().newXPath();
		//On recuperer l'user qui a l'id en parametre de la fonction
		NodeList recherche=(NodeList)xPath.evaluate("/connection/compte[@id="+id+"]",document.getDocumentElement(),XPathConstants.NODESET);
		//On recupere les information de cette id
		Element user=(Element) recherche.item(0);
		String username= user.getElementsByTagName("user").item(0).getTextContent();
		Element localisation=(Element) user.getElementsByTagName("position").item(0);
		String longitude=localisation.getElementsByTagName("longitude").item(0).getTextContent();
		String latitude=localisation.getElementsByTagName("latitude").item(0).getTextContent();
		return new User(username,id,new Position(longitude, latitude));
	}
	/**
	 * Fonction qui ajoute la visibilite de la personne demander
	 * @param idvasuivre id de la personne qui demande la localisation
	 * @param idsuivie id de la personne qui sera localiser
	 * @throws XPathExpressionException 
	 * @throws IOException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 */
	public void acceptDemande(String idvasuivre,String idsuivie) throws XPathExpressionException, TransformerFactoryConfigurationError, TransformerException, IOException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList recherche=(NodeList)xPath.evaluate("/connection/compte[@id="+idvasuivre+"]/abonnees",document.getDocumentElement(),XPathConstants.NODESET);
		//Si il existe deja un abonnement alors on le rajoute pas
		if (((NodeList)xPath.evaluate("/connection/compte[@id="+idvasuivre+"]/abonnees[subscriber="+idsuivie+"]",document.getDocumentElement(),XPathConstants.NODESET)).getLength()!=0){
			return;
		}
		Element user=(Element)recherche.item(0);
		//On prepare l'abonnement
		Element abonne=document.createElement("subscriber");
		abonne.setTextContent(idsuivie);
		user.appendChild(abonne);
		
		updateFile();
	}		

	

}
