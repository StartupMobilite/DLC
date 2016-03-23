package com.app.dlc.dlc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Distributeur_action extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributeur_action);
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
        else if (id == R.id.action_signup){
            Intent intent = new Intent(this, Signup_user.class);
            startActivity(intent);
        }
        else if (id == R.id.action_auth){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
