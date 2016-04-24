package com.example.pji.mapspji.api.dom.src.dom;



import com.example.pji.mapspji.api.dom.src.Exception.UserIsUsedException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DomAddUser extends DomConnection {

    public DomAddUser(File filexml) throws ParserConfigurationException, SAXException, IOException {
        super(filexml);

    }

    public void addUser(String username, String password,Boolean localiser)
            throws TransformerFactoryConfigurationError, TransformerException, IOException, XPathExpressionException, UserIsUsedException {
        //Si l'user n'est est pas disponible alors renvoyer une exception
        if (!verifUserName(username)) {
            throw new UserIsUsedException();
        }
        // Creation de l'element compte
        Element compte = document.createElement("compte");
        // Creation de l'attribut id et ajouter au compte
        int id = getAllUser().size() + 1;
        compte.setAttribute("id", "" + id);
        // Creation de l'element user et ajout
        Element user = document.createElement("user");
        user.setTextContent(username);
        compte.appendChild(user);
        // Creation de l'element password et ajout
        Element pass = document.createElement("pass");
        pass.setTextContent(password);
        compte.appendChild(pass);
        // Creation de l'element position et le droit de partage ou pas(en
        // attributs)
        Element position = document.createElement("position");
        Element longitude = document.createElement("longitude");
        Element latitude = document.createElement("latitude");
        position.appendChild(longitude);
        position.appendChild(latitude);
        if (localiser)
            position.setAttribute("localis", "yes");
        else
            position.setAttribute("localis", "no");
        compte.appendChild(position);
        // Creation de l'element abonnees qui contiendra les ids,pour lesquelle
        // le compte peux voir les deplacements
        Element autoriser = document.createElement("abonnees");
        compte.appendChild(autoriser);
        // Demande de Localisation
        Element demande = document.createElement("Demande");
        compte.appendChild(demande);
        racine.appendChild(compte);
        // Mise a jour du fichier
        updateFile();
    }

    public boolean verifUserName(String name) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        String recherche = (String) xPath.evaluate("count(//compte[user='" + name + "'])", document.getDocumentElement(), XPathConstants.STRING);
        if (recherche.equals("0")) {
            return true;
        }
        return false;

    }


}
