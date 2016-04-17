package com.app.dlc.dlc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.activity.home;
import com.app.dlc.dlc.model.Categorie;
import com.app.dlc.dlc.model.Distributeur;
import com.app.dlc.dlc.model.LoggedInUser;
import com.app.dlc.dlc.model.Produit;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class detail extends AppCompatActivity {
    TextView nom,tv_prixinitial,tv_prixfinal,tv_dlc,tv_categorie,tv_quantite;
    TextView tv_nomDistributeur,tv_addresseDistributeur;
    ImageView image;
    ExpandableRelativeLayout expandableLayoutInfoDistributeur;
    String idDistributeur;
    Distributeur distributeur;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        distributeur= new Distributeur();

        image = (ImageView) findViewById(R.id.backdrop);

        tv_prixinitial = (TextView) findViewById(R.id.tv_prixinitial);
        tv_prixfinal = (TextView) findViewById(R.id.tv_prixfinal);
        tv_dlc = (TextView) findViewById(R.id.tv_dlc);
        tv_categorie = (TextView) findViewById(R.id.tv_categorie);
        tv_quantite = (TextView) findViewById(R.id.tv_quantite);
        tv_nomDistributeur = (TextView) findViewById(R.id.tv_nomDistributeur);
        tv_addresseDistributeur = (TextView) findViewById(R.id.tv_addresseDistributeur);
        tv_quantite = (TextView) findViewById(R.id.tv_quantite);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+tv_addresseDistributeur.getText()));
                startActivity(intent);
            }
        });




        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String nom = getIntent().getExtras().getString("Nom");
        idDistributeur = getIntent().getExtras().getString("IdDistributeur");
        tv_prixinitial.setText(getIntent().getExtras().getString("PrixInitial"));
        tv_prixfinal.setText(getIntent().getExtras().getString("PrixFinal"));
        tv_dlc.setText(getIntent().getExtras().getString("Dlc"));
        tv_categorie.setText(getIntent().getExtras().getString("Categorie"));
        tv_quantite.setText(getIntent().getExtras().getString("Quantite"));

        /*String prixFinal = getIntent().getExtras().getString("PrixFinal");
        String dlc = getIntent().getExtras().getString("Dlc");
        String categorie = getIntent().getExtras().getString("Categorie");
        String quantite = getIntent().getExtras().getString("Quantite");
        */
        byte[] imageAsBytes = getIntent().getExtras().getByteArray("Image");
        image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(nom);
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        GetDistributeurInformationAsyncTask task = new GetDistributeurInformationAsyncTask();

        //execute la tache asynchrone
        String url ="https://dlcapi.herokuapp.com/api/Distributeurs?filter[where][id]="+idDistributeur+"&access_token="+LoggedInUser.token;
        task.execute(url);
    }
    public void toggleInfoDistributeur(View view) {
        expandableLayoutInfoDistributeur = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutInfoDistributeur);
        expandableLayoutInfoDistributeur.toggle(); // toggle expand and collapse
    }

    public class GetDistributeurInformationAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Integer doInBackground(String... params) {
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
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d("Erreur", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {
                tv_nomDistributeur.setText(distributeur.getNom());
                tv_addresseDistributeur.setText(distributeur.getAddresse());


            } else {
                Log.e("Erreur", "Failed to fetch data!");
            }
        }
        private void parseResult(String result) {
            try {
                JSONArray response = new JSONArray(result);


                for(int i =0;i<response.length();i++)
                {
                    JSONObject object = response.getJSONObject(i);
                    //distributeur = new Distributeur();
                    distributeur.setNom(object.getString("nom"));
                    distributeur.setId(object.getInt("id"));
                    distributeur.setAddresse(object.getString("voie")+","+object.getString("ville")+","+object.getString("codepostal"));
                    distributeur.setHoraireOuverture(object.getString("horaireOuverture"));
                    distributeur.setHoraireFermerture(object.getString("horaireFermerture"));
                    distributeur.setImage(object.getString("image"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
