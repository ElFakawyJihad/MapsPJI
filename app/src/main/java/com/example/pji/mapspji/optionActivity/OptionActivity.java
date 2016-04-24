package com.example.pji.mapspji.optionActivity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pji.mapspji.MapsActivity;
import com.example.pji.mapspji.R;

public class OptionActivity extends AppCompatActivity {

    private CoordinatorLayout coordinator;
    private String username;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutOption);
        Intent user=getIntent();
        long raf=user.getLongExtra("raf",8000);
        username=user.getStringExtra("user");
        id=user.getIntExtra("id", 0);
        final EditText editRaf=(EditText)findViewById(R.id.editTextRaf);
        editRaf.setText(""+raf/1000);
        Button button=(Button)findViewById(R.id.buttonRaf);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long temps=Long.parseLong(editRaf.getText().toString());
                if (temps<8){
                    Snackbar snackbar = Snackbar
                            .make(coordinator, "Temps de rafraichissement doit etre Superieur à 8 secondes", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else{
                    Intent intent=new Intent(OptionActivity.this, MapsActivity.class);
                    intent.putExtra("raf",temps*1000);
                    intent.putExtra("user",username);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            }
        });

    }

}
