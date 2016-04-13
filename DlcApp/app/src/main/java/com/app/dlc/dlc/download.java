package com.app.dlc.dlc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.dlc.dlc.activity.home;
import com.app.dlc.dlc.model.LoggedInUser;
import com.app.dlc.dlc.model.Produit;

import org.json.JSONArray;
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

public class download extends AppCompatActivity {
    private ProgressDialog dialog;
    private static final String TAG = "RecyclerViewExample";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

    }

    public void loadImagefromGallery(View view) {
        final String url = "https://dlcapi.herokuapp.com/api/Produits?filter[where][idproduit]=4";
        new AsyncHttpTask().execute(url);

    }

    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);

            JSONObject object = response.getJSONObject(0);
            String image = object.getString("image");
            byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
            ImageView imgView = (ImageView)this.findViewById(R.id.imgView);
            imgView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming th java.net.URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    return response.toString();
                }else{
                    return "Erreur";
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
                return "Erreur";
            }

        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            /* Download complete. Lets update UI */
            if (!result.equals("Erreur")) {
                parseResult(result.toString());

            } else {
                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }
}
