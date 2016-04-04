package com.app.dlc.dlc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
                d.execute(url);
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

            // params comes from the execute() call: params[0] is the url.
            try {
                return loginRequest(urls[0],email,password);
            } catch (Exception e) {
                Log.d(DEBUG_TAG, "The response is: " + e.toString());
                return e.toString();
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String responseCode) {
            dialog.dismiss();

            if(responseCode.equals("200"))
            {
                Intent intent = new Intent(getApplicationContext(), home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                intent.putExtra("token", token);
                // supprimer l activite du stack
                startActivityForResult(intent, 0);
            }
            else
            {
                String ss=responseCode.toString();
                //Toast.makeText(getApplicationContext(), "Identifiants invalides", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), responseCode.toString(), Toast.LENGTH_LONG).show();

            }
        }


        private String loginRequest(String requestUrl,String email, String password) throws IOException {
            JSONObject credentials = new JSONObject();
            try{
                credentials.put("email", "guillaumeyvo@yahoo.fr");
                credentials.put("password", "1234");
                //credentials.put("email", email);
                //credentials.put("password", password);
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
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();
                urlConnection.connect();
                int response = urlConnection.getResponseCode();
                resultheader = Integer.toString(response) ;
                InputStream inputStream ;
                if (response == 200)
                {
                    //inputStream = urlConnection.getInputStream();
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder jsonResponse = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        jsonResponse.append(line);
                    }
                    parseResult(jsonResponse.toString());
                    LoggedInUser.type = "utilisateur";
                }
                else
                {
                    url = new URL("https://dlcapi.herokuapp.com/api/utilisateurs/login");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    // is output buffer writter
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                    writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                    writer.write(JsonDATA);
// json data
                    writer.close();
                    urlConnection.connect();
                    response = urlConnection.getResponseCode();
                    resultheader = Integer.toString(response) ;
                    if (response == 200)
                    {
                        //inputStream = urlConnection.getInputStream();
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
                //return convertStream(inputStream);

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return  e.toString();
            }
        }

        private void parseResult(String result) {
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
