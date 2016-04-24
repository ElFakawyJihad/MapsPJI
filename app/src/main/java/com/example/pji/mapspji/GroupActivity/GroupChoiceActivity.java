package com.example.pji.mapspji.GroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.database.groupe.Group;

import java.sql.SQLException;
import java.util.HashMap;

public class GroupChoiceActivity extends AppCompatActivity {

    private  int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_choice);
        ArrayAdapter<String> adapter;
        final HashMap<String,Integer> mapGroupe ;
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //----------------------------------------

        Intent intent=getIntent();
        final String user=intent.getStringExtra("user");
        id=intent.getIntExtra("id",0);
        //-----------------------------------------------------------
        ListView groupes = (ListView) findViewById(R.id.groupeChoice);
        //---Si l'utilisateur veux s'inscrire apres à un groupe
        final Button passer=(Button)findViewById(R.id.passer);
        passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChoiceActivity.this, MapsActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        final Button creerGroupe=(Button)findViewById(R.id.pageCreerGroupe);
        creerGroupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChoiceActivity.this, CreateGroupActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        //---------------------------------------------------

        try {
            Group groupe = new Group();
            //On recupere les groupes de la base de donnee
            mapGroupe=groupe.getAllGroup();
            String[] listesGroupe=mapGroupe.keySet().toArray(new String[0]);
            adapter = new ArrayAdapter<String>(GroupChoiceActivity.this,
                    android.R.layout.simple_list_item_1,listesGroupe);
            groupes.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //-----Lorsque un element est cliquer sur la liste
        groupes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nomGroupe=(String)parent.getItemAtPosition(position);
                try {
                    Group groupe = new Group();
                    //On recupere l'ensemble des groupes actuels
                    HashMap<String, Integer> map = groupe.getAllGroup();
                    //Recupere l'id du groupe
                    int idGroupe=map.get(nomGroupe);
                    Intent intent = new Intent(GroupChoiceActivity.this, ConfirmGroupActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("id", GroupChoiceActivity.this.id);
                    intent.putExtra("nomGroupe",nomGroupe);
                    intent.putExtra("groupe",idGroupe);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
