package com.example.pji.mapspji.GroupActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;
import com.example.pji.mapspji.database.groupe.GroupUser;

public class ConfirmUnsubscribeGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_unsubscribe_group);
        //-------------------Recuperer les valeurs de l'Intent----
        Intent intent=getIntent();
        final String username=intent.getStringExtra("user");
        final int iduser=intent.getIntExtra("id",0);
        String nomGroupe=intent.getStringExtra("nomGroupe");
        final int idgroupe= intent.getIntExtra("groupe",0);
        final long rafraichissement = intent.getLongExtra("raf", 8000);
        //Ces deux lignes sont a rajouter pour l'envoir de requete Html seulement
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //----------------------------------------
        TextView desabonnerText=(TextView)findViewById(R.id.textViewUnsubscribeGroup);
        desabonnerText.setText("Voulez-vous vous desabonner du groupe "+nomGroupe+" ?");
        Button unsubscribe=(Button)findViewById(R.id.unsubscribeButton);
        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new GroupUser().deleteInGroupe(iduser,idgroupe);
                    Intent intent=new Intent(ConfirmUnsubscribeGroupActivity.this, MapsActivity.class);
                    intent.putExtra("user",username);
                    intent.putExtra("id",iduser);
                    intent.putExtra("raf",rafraichissement);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
