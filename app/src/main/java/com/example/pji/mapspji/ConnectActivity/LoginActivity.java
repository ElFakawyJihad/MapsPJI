package com.example.pji.mapspji.ConnectActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.database.base.User;
import com.example.pji.mapspji.database.exception.UserOrPasswordFalseException;

import java.io.File;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private final static String NAMEFILECONNECTION = "connection.xml";
    private File fileConnection;
    // UI references.
    private TextView mNameUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //----------------------------------------
        setContentView(R.layout.activity_login);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        // Set up the login form.
        mNameUserView = (TextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mUsernameSignInButton = (Button) findViewById(R.id.sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //Creation de Login
        final TextView text = (TextView) findViewById(R.id.creerCompte);
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setTextColor(Color.BLUE);
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid name, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mNameUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mNameUserView.getText().toString();
        String password = mPasswordView.getText().toString();

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
            try {
                //Maintenant on verifie les donnees de connexion
                int id=new User().identification( user, password);
                //Si tout ce passe bien on accede au compte de l'utilisateur
                Intent intent = new Intent(this, MapsActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("id",id);
                startActivity(intent);
                //Si on a un probleme alors une erreur d'identifiant sera capter
            } catch (UserOrPasswordFalseException e) {
                mPasswordView.setText(null);
                Snackbar snackbar = Snackbar
                        .make(coordinator, "Utilisateur ou Mot de passe incorrect", Snackbar.LENGTH_LONG);

                snackbar.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    //On bloque le bouton de Retour
    public void onBackPressed() {
        // do nothing.
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

