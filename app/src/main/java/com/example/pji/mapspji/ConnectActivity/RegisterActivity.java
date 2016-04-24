package com.example.pji.mapspji.ConnectActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pji.mapspji.GroupActivity.GroupChoiceActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.api.dom.src.Exception.UserIsUsedException;
import com.example.pji.mapspji.database.base.User;
import com.example.pji.mapspji.database.exception.NameIsUseException;
import com.example.pji.mapspji.ftp.SendDownloadFileFTP;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class RegisterActivity extends AppCompatActivity {

    private final static String NAMEFILECONNECTION = "connection.xml";
    private File fileConnection=null;
    // UI references.
    private TextView mNameUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CoordinatorLayout coordinator;
    private SendDownloadFileFTP ftp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //----------------------------------------
        setContentView(R.layout.activity_register_in);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutRegister);
        // Set up the login form.
        mNameUserView = (TextView) findViewById(R.id.username_sign_in);
        mPasswordView = (EditText) findViewById(R.id.password_sign_in);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    try {
                        attemptLogin();
                    } catch (Exception e) {
                        Log.i("Erreur autre", "aaa");
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });

        Button registerInButton = (Button) findViewById(R.id.register_in_button);
        registerInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    attemptLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid name, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() throws FTPIllegalReplyException, FTPDataTransferException, FTPException, FTPAbortedException, IOException, ParserConfigurationException, SAXException, TransformerException, XPathExpressionException, UserIsUsedException {

        // Reset errors.
        mNameUserView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        final String user = mNameUserView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            //On renvoie une erreur si le mot de passe contient  moins de 4 caractere et si il ne contient rien
            mPasswordView.setError("Mot de passe trop court");
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user .
        if (TextUtils.isEmpty(user)) {
            mNameUserView.setError("Besoin d'un nom d'utilisateur");
            focusView = mNameUserView;
            cancel = true;
            //Verifie si c 'un user valide
        } else if (!isNameValid(user)) {
            mNameUserView.setError("Nom d'utilisateur trop court");
            focusView = mNameUserView;
            cancel = true;
            //Verifie si le password n'est pas vide
        } else if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Mot de passe indispensable");
            focusView = mPasswordView;
            cancel = true;
        }
        //Si erreur alors on l'affiche
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
                //Maintenant on ajoute l'utilisateur
                        try {
                            int id=new User().createUser(user, password);
                            //Si tout ce passe bien on accede au compte de l'utilisateur
                            Intent intent = new Intent(RegisterActivity.this, GroupChoiceActivity.class);
                            intent.putExtra("user", user);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            //Si on a un probleme alors une erreur d'identifiant sera capter
                        }catch (NameIsUseException  e) {
                            Snackbar.make(coordinator, "Nom d'Utilisateur deja utilise", Snackbar.LENGTH_LONG).show();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
        }

    }

    private boolean isNameValid(String name) {
        return name.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    protected void onPostExecute(final Boolean success) {

        if (success) {
            finish();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }
}



