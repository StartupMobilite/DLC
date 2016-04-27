package com.app.dlc.dlc;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dlc.dlc.model.Distributeur;
import com.app.dlc.dlc.model.LoggedInUser;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class detail extends AppCompatActivity {
    TextView nom,tv_prixinitial,tv_prixfinal,tv_dlc,tv_categorie,tv_quantite;
    TextView tv_nomDistributeur,tv_voie,tv_ville_codePostal;
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
        tv_voie = (TextView) findViewById(R.id.tv_voie);
        tv_ville_codePostal = (TextView) findViewById(R.id.tv_ville_codePostal);
        tv_quantite = (TextView) findViewById(R.id.tv_quantite);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+tv_voie.getText()));
                startActivity(intent);
            }
        });




        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String nom = getIntent().getExtras().getString("Nom");
        idDistributeur = getIntent().getExtras().getString("IdDistributeur");
        tv_prixinitial.setText(getIntent().getExtras().getString("PrixInitial"));
        tv_prixinitial.setPaintFlags(tv_prixinitial.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
                tv_voie.setText(distributeur.getVoie());
                tv_ville_codePostal.setText(distributeur.getVille()+", "+distributeur.getCodePostal());


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
                    distributeur.setVoie(object.getString("voie"));
                    distributeur.setVille(object.getString("ville"));
                    distributeur.setCodePostal(object.getString("codepostal"));
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
