package com.app.dlc.dlc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.*;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home){
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
        else if (id == R.id.action_auth){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
