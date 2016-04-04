package com.app.dlc.dlc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.*;
import android.content.Intent;

public class Authentication extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Button btidentifier = (Button) findViewById(R.id.btindentifier); // obtenir la reference sur le button identifier

        btidentifier.setOnClickListener(new View.OnClickListener() { // on attribue un click listener sur le button identifier
            public void onClick(View v) {
                //on cree un intent. Un intent est en gros le moyen utliser par android pour passer d une activite a une autre
                // on definit l intent de la maniere suivante de l activite actuelle vers l activite search
                Intent intent = new Intent(Authentication.this,Search.class);
                Authentication.this.startActivity(intent); // enclenche l intent
            }
        });


    }
}
