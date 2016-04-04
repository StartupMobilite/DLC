package com.app.dlc.dlc;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Async_example extends AppCompatActivity {


    private ProgressBar mProgressBar;

    private TextView tvresult;
    private TextView tvheader;
    private TextView tvpostresult;
    private TextView tvpostresultheader;

    private Button mButton;
    private Button btnAsync;
    private Button btpost;
    private Button btchangelayout;

    private static final String DEBUG_TAG = "HttpExample";
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_example);

        // On récupère les composants de notre layout
        mProgressBar = (ProgressBar) findViewById(R.id.pBAsync);

        mButton = (Button) findViewById(R.id.btnLaunch);
        btnAsync = (Button) findViewById(R.id.btnAsync);
        btpost = (Button) findViewById(R.id.btpost);
        btchangelayout = (Button) findViewById(R.id.btchangelayout);

        tvresult = (TextView) findViewById(R.id.tvresult);
        tvheader = (TextView) findViewById(R.id.tvheader);
        tvpostresult = (TextView) findViewById(R.id.tvpostresult);
        tvpostresultheader = (TextView) findViewById(R.id.tvpostresultheader);


        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        // On met un Listener sur le bouton
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BigCalcul calcul = new BigCalcul();
                calcul.execute();
            }
        });

        btnAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String url = "https://dlcapi.herokuapp.com/api/Produits";
                DownloadWebpageTask d = new DownloadWebpageTask();
                d.execute(url);
            }
        });

        btpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String url = " https://dlcapi.herokuapp.com/api/utilisateurs";
                PostJsonTask d = new PostJsonTask();
                d.execute(url);
            }
        });
        btchangelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                setContentView(R.layout.activity_create_account);
            }
        });
    }

    private class BigCalcul extends AsyncTask<Void, Integer, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Début du traitement asynchrone", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            // Mise à jour de la ProgressBar
            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int progress;
            for (progress=0;progress<=100;progress++)
            {
                for (int i=0; i<100000000; i++){}
                //la méthode publishProgress met à jour l'interface en invoquant la méthode onProgressUpdate
                publishProgress(progress);
                progress++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), "Le traitement asynchrone est terminé", Toast.LENGTH_LONG).show();
        }
    }


    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
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
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            tvresult.setText(result);
            tvheader.setText(resultheader);


        }


        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                resultheader = Integer.toString(response) ;
                //resultheader = conn.getResponseMessage();
                Log.d(DEBUG_TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                //String contentAsString = readIt(is, len);


                //return contentAsString;

                return convertStream(is);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }

            finally {
                if (is != null) {
                    is.close();
                }
            }
        }




    }

    private class PostJsonTask extends AsyncTask<String, Void, String> {
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
                return PostJson(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            tvpostresult.setText(result);
            tvpostresultheader.setText(resultheader);


        }


        private String PostJson(String myurl) throws IOException {

            JSONObject post_dict = new JSONObject();
            try{
                post_dict.put("ville" , "Babi");
                post_dict.put("codepostal", "225");
                post_dict.put("email", "guillaumeyvo@gmail.com");
                post_dict.put("password", "123456");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String JsonResponse = null;
            String JsonDATA = String.valueOf(post_dict);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://dlcapi.herokuapp.com/api/utilisateurs");
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
                InputStream inputStream = urlConnection.getErrorStream();


                return convertStream(inputStream);

            }
            catch (Exception e)
            {
                e.printStackTrace();
                return  e.toString();
            }


        }

        private String PostJson2(String myurl) throws IOException {

            JSONObject post_dict = new JSONObject();
            try{
                post_dict.put("ville" , "Babi");
                post_dict.put("codepostal", "225");
                post_dict.put("email", "guillaumeyvo@gmail.com");
                post_dict.put("password", "123456");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String JsonResponse = null;
            String JsonDATA = String.valueOf(post_dict);
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://dlcapi.herokuapp.com/api/utilisateurs");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
//set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
// json data
                writer.close();
                InputStream inputStream = urlConnection.getInputStream();
//input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
//response data
                try {
//send to post execute
                    return JsonResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;



            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(DEBUG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }

        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        public String convertStream(InputStream stream) throws IOException, UnsupportedEncodingException {
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line ="";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            return  buffer.toString();
        }
    }



