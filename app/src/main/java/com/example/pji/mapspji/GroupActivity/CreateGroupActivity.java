package com.example.pji.mapspji.GroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.database.exception.GroupNameIsUseException;
import com.example.pji.mapspji.database.groupe.Group;
import com.example.pji.mapspji.database.groupe.GroupUser;

public class CreateGroupActivity extends AppCompatActivity {

    private long rafraichissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        final CoordinatorLayout coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutCreateGroupe);
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //-----------------On recupere les valeurs de user et id-----------------------
        Intent intent=getIntent();
        final String user=intent.getStringExtra("user");
        final int id=intent.getIntExtra("id",0);
        rafraichissement =intent.getLongExtra("raf",8000);
        //----------------------------------------
        final EditText nomGroupe=(EditText)findViewById(R.id.nomGroupe);
        Button buttoncreer=(Button)findViewById(R.id.creerGroupe);
        buttoncreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nomGroupe.getText().toString().length()<4){
                    Snackbar.make(coordinator, "Nom de Groupe trop court (minimum 4 caractere)", Snackbar.LENGTH_LONG).show();
                }
                else{
                    try {
                        Group group= new Group();
                        String nom=nomGroupe.getText().toString();
                        group.addNewGroup(nom);
                        int idgroupe=group.getAllGroup().get(nom);
                        new GroupUser().addInGroupe(id,idgroupe);
                        Intent intent = new Intent(CreateGroupActivity.this, MapsActivity.class);
                        intent.putExtra("user", user);
                        intent.putExtra("id", id);
                        intent.putExtra("raf",CreateGroupActivity.this.rafraichissement);
                        startActivity(intent);
                    }catch (GroupNameIsUseException e) {
                        Snackbar.make(coordinator, "Nom de Groupe deja utilisé", Snackbar.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
