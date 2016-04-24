package com.example.pji.mapspji;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.pji.mapspji.api.dom.src.dom.DomUser;
import com.example.pji.mapspji.api.dom.src.user.User;
import com.example.pji.mapspji.ftp.SendDownloadFileFTP;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AddFriendUserActivity extends AppCompatActivity {

    private String username;
    private String pass;
    private String[] users;
    private AutoCompleteTextView auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_user);
        Intent intent=getIntent();
        username =intent.getStringExtra("user");
        pass=intent.getStringExtra("pass");
        auto=(AutoCompleteTextView)findViewById(R.id.listUser);
        timerUsers();


    }
    protected void timerUsers(){
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SendDownloadFileFTP ftp = new SendDownloadFileFTP();
                            File fileConnection = ftp.download("connection.xml");
                            DomUser user = new DomUser(fileConnection, username, pass);
                            users = getStringUsers(user.getAllUser());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddFriendUserActivity.this, android.R.layout.simple_list_item_1, users);
                            auto.setAdapter(adapter);
                            ftp.upload(fileConnection);
                            ftp.disconnect();
                        } catch (Exception e) {
                            Log.e("eeeeeee", e.toString());
                        }
                    }
                });
            }
        }, 0, 1000000);
    }

    private String[] getStringUsers(ArrayList<User> allUser) {
        int size=allUser.size();
        String[] retour=new String[size];
        for (int i=0;i<size;i++){
            retour[i]=allUser.get(i).getUsername();
        }
        return retour;
    }



}
