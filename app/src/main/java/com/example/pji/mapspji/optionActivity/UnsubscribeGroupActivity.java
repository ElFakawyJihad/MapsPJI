package com.example.pji.mapspji.optionActivity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pji.mapspji.GroupActivity.ConfirmGroupActivity;
import com.example.pji.mapspji.GroupActivity.ConfirmUnsubscribeGroupActivity;
import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.database.groupe.Group;
import com.example.pji.mapspji.database.groupe.GroupUser;

import java.sql.SQLException;
import java.util.HashMap;

public class UnsubscribeGroupActivity extends AppCompatActivity {

    private int id;
    private String user;
    private long rafraichissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsubscribe_group);
        ListView groupes=(ListView)findViewById(R.id.listViewdesabonnement);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        user=intent.getStringExtra("user");
        rafraichissement =intent.getLongExtra("raf",8000);
        Button retour=(Button)findViewById(R.id.buttonRetour2);
        try {
            GroupUser user=new GroupUser();
            HashMap<String,Integer> map=user.getMyGroup(id);
            String[] listesGroupe=map.keySet().toArray(new String[0]);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(UnsubscribeGroupActivity.this,
                    android.R.layout.simple_list_item_1, listesGroupe);
            groupes.setAdapter(adapter);
            //-----Lorsque un element est cliquer sur la liste
            groupes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String nomGroupe=(String)parent.getItemAtPosition(position);
                    try {
                        GroupUser groupe = new GroupUser();
                        //On recupere l'ensemble des groupes pour lesquelles on peux se desabonner
                        HashMap<String, Integer> map = groupe.getMyGroup(UnsubscribeGroupActivity.this.id);
                        //Recupere l'id du groupe
                        int idGroupe=map.get(nomGroupe);
                        Intent intent = new Intent(UnsubscribeGroupActivity.this, ConfirmUnsubscribeGroupActivity.class);
                        intent.putExtra("user", UnsubscribeGroupActivity.this.user);
                        intent.putExtra("id", UnsubscribeGroupActivity.this.id);
                        intent.putExtra("nomGroupe",nomGroupe);
                        intent.putExtra("raf",UnsubscribeGroupActivity.this.rafraichissement);
                        intent.putExtra("groupe",idGroupe);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnsubscribeGroupActivity.this, MapsActivity.class);
                intent.putExtra("user", UnsubscribeGroupActivity.this.user);
                intent.putExtra("id", UnsubscribeGroupActivity.this.id);
                intent.putExtra("raf",UnsubscribeGroupActivity.this.rafraichissement);
                startActivity(intent);
            }
        });
    }
}
