package com.app.dlc.dlc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class login extends AppCompatActivity {
    @Bind(R.id.link_signup)TextView _signupLink;
    @Bind(R.id.btn_login)Button _loginButton;
    private ProgressDialog dialog;
    EditText input_email;
    EditText input_password;
    String email;
    String password;
    String token;
    private static final String DEBUG_TAG = "LOGIN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        email =input_email.getText().toString();
        password=input_password.getText().toString();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
/*        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        // definition du progress bar qui s affichera pendant la requete d authentification
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");



        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "https://dlcapi.herokuapp.com/api/Produits";
                email =input_email.getText().toString();
                password=input_password.getText().toString();
                LoginAsyncTask d = new LoginAsyncTask();

                //execute la tache asynchrone
                d.execute(url);

                //ordre d execution de toute tache asynchrone onPreExecute => doInBackground => onPostExecute
            }
        });
    }

    private class LoginAsyncTask extends AsyncTask<String, Void, String> {
        String resultheader="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {

            try {
                return loginRequest(urls[0],email,password);
            } catch (Exception e) {
                Log.d(DEBUG_TAG, "The response is: " + e.toString());
                return e.toString();
            }
        }

        @Override
        protected void onPostExecute(String responseCode) {
            dialog.dismiss(); //masque le dialg box

            if(responseCode.equals("200")) // si l'authentification a reussi
            {
                // definition de l intent pour demarrer une nouvelle activite
                Intent intent = new Intent(getApplicationContext(), home.class);
                // supprimer l activite du backstack pour eviter un retour vers la page de login
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(intent, 0); // execution de l intent
            }
            else //si l authentification echoue
            {
                Toast.makeText(getApplicationContext(), "Identifiants invalides", Toast.LENGTH_LONG).show();
            }
        }


        private String loginRequest(String requestUrl,String email, String password) throws IOException {
            JSONObject credentials = new JSONObject();
            try{

                // creation du json a passer pour la requete vers l api

                // donnees en dur pour eviter de retaper les credentials a chaque test

                //Credential User
                credentials.put("email", "guillaumeyvo@yahoo.fr");
                credentials.put("password", "1234");

                //Credential Distributeur
//                credentials.put("email", "lidl@wyz.fr");
//                credentials.put("password", "12345");

                //A decommenter pour la version finale
                /*=======================================*/
                //credentials.put("email", email);
                //credentials.put("password", password);
                /*=======================================*/
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String JsonResponse = null;
            String JsonDATA = String.valueOf(credentials);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://dlcapi.herokuapp.com/api/utilisateurs/login");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                //definit le type de requete (POST/GET)
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //"ecrit" le json dans les header de la requete
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                writer.close();
                urlConnection.connect();
                int response = urlConnection.getResponseCode(); // recupere le code de reponse suite a la requete effectuee
                resultheader = Integer.toString(response) ;
                InputStream inputStream ;
                if (response == 200) // si l utlisateur est authentifie
                {
                    // recupere la reponse du server en l occurence le json definissant le token, le user id,...
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder jsonResponse = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        jsonResponse.append(line);
                    }
                    parseResult(jsonResponse.toString()); // parse le json

                    //definit le type d utilisateur dans la classe statique LogginUser
                    // le type sera utlise plus tard en vu de definit les prochaines actions a executer en fonction du type de
                    // l utlisateur authentife
                    LoggedInUser.type = "utilisateur";
                }
                else //en cas d echec on teste si c est un distributeur en se basant sur le meme principe
                {
                    url = new URL("https://dlcapi.herokuapp.com/api/Distributeurs/login");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    // is output buffer writter
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                    writer.write(JsonDATA);

                    writer.close();
                    urlConnection.connect();
                    response = urlConnection.getResponseCode();
                    resultheader = Integer.toString(response) ;
                    if (response == 200) // si c'est un distributeur
                    {

                        BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder jsonResponse = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            jsonResponse.append(line);
                        }
                        parseResult(jsonResponse.toString());
                        LoggedInUser.type = "distributeur";

                    }
                    
                }

                return resultheader;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return  e.toString();
            }
        }

        private void parseResult(String result) {
            // parsage du json
            try {
                JSONObject response = new JSONObject(result);
                LoggedInUser.token=response.getString("id");
                LoggedInUser.id=response.getString("userId");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
