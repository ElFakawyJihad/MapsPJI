package com.example.pji.mapspji.optionActivity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.SendMail;
import com.example.pji.mapspji.database.admin.Admin;

import java.io.IOException;
import java.sql.SQLException;

public class AdminActivity extends AppCompatActivity {

    private String username;
    private int id;
    private CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutAdmin);
        Intent user=getIntent();
        final long raf=user.getLongExtra("raf",8000);
        username=user.getStringExtra("user");
        id=user.getIntExtra("id", 0);
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //----------------------------------------

        //---------------Recuperer les Widget------------------
        final EditText mail=(EditText)findViewById(R.id.editTextMail);
        Button admin=(Button) findViewById(R.id.buttonDemandeAdmin);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mail.getText().toString();
                if (isEmailValid(email)){
                    try {
                        new Admin().sendMail(email,username);
                        Intent intent=new Intent(AdminActivity.this, MapsActivity.class);
                        intent.putExtra("raf",raf);
                        intent.putExtra("user",username);
                        intent.putExtra("id",id);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar snackbar = Snackbar
                            .make(coordinator, "Adresse Email Invalide", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
