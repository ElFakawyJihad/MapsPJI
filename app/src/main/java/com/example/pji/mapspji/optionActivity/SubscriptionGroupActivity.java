package com.example.pji.mapspji.optionActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.pji.mapspji.GroupActivity.ConfirmGroupActivity;
import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.database.groupe.Group;
import com.example.pji.mapspji.database.groupe.GroupUser;

import java.util.HashMap;

public class SubscriptionGroupActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private int id;
    private String user;
    private long rafraichissement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_group);
        ListView groupes=(ListView)findViewById(R.id.listViewAbonnement);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        user=intent.getStringExtra("user");
        rafraichissement =intent.getLongExtra("raf",8000);
        Button retour=(Button)findViewById(R.id.buttonRetour);
        try {
            GroupUser user=new GroupUser();
            HashMap<String,Integer> map=user.getOtherGroup(id);
            String[] listesGroupe=map.keySet().toArray(new String[0]);
            adapter = new ArrayAdapter<String>(SubscriptionGroupActivity.this,
                    android.R.layout.simple_list_item_1,listesGroupe);
            groupes.setAdapter(adapter);
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
                        Intent intent = new Intent(SubscriptionGroupActivity.this, ConfirmGroupActivity.class);
                        intent.putExtra("user", SubscriptionGroupActivity.this.user);
                        intent.putExtra("id", SubscriptionGroupActivity.this.id);
                        intent.putExtra("nomGroupe",nomGroupe);
                        intent.putExtra("raf",SubscriptionGroupActivity.this.rafraichissement);
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
                Intent intent = new Intent(SubscriptionGroupActivity.this, MapsActivity.class);
                intent.putExtra("user", SubscriptionGroupActivity.this.user);
                intent.putExtra("id", SubscriptionGroupActivity.this.id);
                intent.putExtra("raf",SubscriptionGroupActivity.this.rafraichissement);
                startActivity(intent);
            }
        });
    }
}
