package com.app.dlc.dlc.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.model.Distributeur;
import com.app.dlc.dlc.model.LoggedInUser;
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
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ModifierUnProduit extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Button btn;
    EditText nom,tv_prixinitial,tv_prixfinal,tv_dlc,tv_categorie,tv_quantite,tv_nomProduit;
    TextView tv_nomDistributeur,tv_addresseDistributeur;
    ImageView image;
    ExpandableRelativeLayout expandableLayoutInfoDistributeur;
    String idDistributeur,idProduit;
    Distributeur distributeur;
    Spinner spinner;
    ArrayAdapter<String> categorie_list;
    ArrayAdapter<String> dataAdapter;
    List<String> categories = new ArrayList<String>();
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Hashtable<String,String> categorieTable;
    //Hashtable categorieTable;
    Enumeration names;
    String url;
    String action;
    TextInputLayout txt_prixInitial,txt_prixFinal,txt_Quantite,txt_nomProduit,txt_dlc;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_un_produit);
        categorieTable=new Hashtable<String,String>();
        //categorieTable=new Hashtable();
        url="";
        action=getIntent().getExtras().getString("Action");


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        distributeur= new Distributeur();

        image = (ImageView) findViewById(R.id.backdrop);
        spinner = (Spinner) findViewById(R.id.categorie_spinner);

        tv_nomProduit = (EditText) findViewById(R.id.tv_nomProduit);
        tv_prixinitial = (EditText) findViewById(R.id.tv_prixinitial);
        tv_prixfinal = (EditText) findViewById(R.id.tv_prixfinal);
        tv_dlc = (EditText) findViewById(R.id.tv_dlc);
        tv_dlc.setInputType(InputType.TYPE_NULL);
        tv_dlc.requestFocus();
        //tv_categorie = (EditText) findViewById(R.id.tv_categorie);
        tv_quantite = (EditText) findViewById(R.id.tv_quantite);

        tv_quantite = (EditText) findViewById(R.id.tv_quantite);


        txt_prixInitial = (TextInputLayout) findViewById(R.id.txt_prixInitial);
        txt_prixFinal = (TextInputLayout) findViewById(R.id.txt_prixFinal);
        txt_Quantite = (TextInputLayout) findViewById(R.id.txt_Quantite);
        txt_nomProduit = (TextInputLayout) findViewById(R.id.txt_nomProduit);
        txt_dlc = (TextInputLayout) findViewById(R.id.txt_dlc);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*  InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(tv_dlc.getWindowToken(), 0);*/


        //if(getIntent().getExtras().getString("Modif"))

        if(action.equals("Modifier"))
        {

            tv_nomProduit.setText(getIntent().getExtras().getString("Nom"));
            idDistributeur = getIntent().getExtras().getString("IdDistributeur");
            idProduit = getIntent().getExtras().getString("Id");
            tv_prixinitial.setText(getIntent().getExtras().getString("PrixInitial"));
            tv_prixfinal.setText(getIntent().getExtras().getString("PrixFinal"));
            tv_dlc.setText(getIntent().getExtras().getString("Dlc"));
            //tv_categorie.setText(getIntent().getExtras().getString("Categorie"));
            tv_quantite.setText(getIntent().getExtras().getString("Quantite"));
            categories.clear();
            categories.add(getIntent().getExtras().getString("Categorie"));
            byte[] imageAsBytes = getIntent().getExtras().getByteArray("Image");
            image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            String cat=getIntent().getExtras().getString("Categorie");
            try{
                url="https://dlcapi.herokuapp.com/api/Categories?filter[where][nom][neq]="+ URLEncoder.encode(cat, "UTF-8")+"&access_token="+LoggedInUser.token;
            }
            catch (Exception e){
                Log.d("Erreur", e.getMessage());
            }
        }
        else
        {
            categories.clear();
            url="https://dlcapi.herokuapp.com/api/Categories?access_token="+LoggedInUser.token;

        }




        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        tv_dlc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ModifierUnProduit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }


        });







        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });





        new DownloasdCategoriesAsyncHttpTask().execute(url);

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        tv_dlc.setText(sdf.format(myCalendar.getTime()));
    }


    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.backdrop);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "Vous n'avez choisi aucune image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Une erreur s est produite", Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            //Toast.makeText(this, "Save",Toast.LENGTH_LONG).show();
            //save();
            if(validation())
            {
                String url ="https://dlcapi.herokuapp.com/api/Produits/"+idProduit;
                UploadAsyncTask uploadAsyncTask =new UploadAsyncTask();
                uploadAsyncTask.execute(url);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //creer une async task pour update le produit dont l id est contenu dans la variable idProduit
    //https://docs.strongloop.com/display/public/LB/PersistedModel+REST+API
    //exemple de requete https://dlcapi.herokuapp.com/api/Produits/5
    //json
    // {
   // "quantite": 41
    //}

    public class DownloasdCategoriesAsyncHttpTask extends AsyncTask<String, Void, Integer> {

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

                //URL url = new URL(java.net.URLEncoder.encode(params[0],"UTF-8"));
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



                        parseCategories(response.toString());
                    result = 1; // Successful
                    }

                else{

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    String s = response.toString();

                }



            } catch (Exception e) {
                Log.d("Message", e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            dataAdapter = new ArrayAdapter<String>(ModifierUnProduit.this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            //spinner.setSelection(0);
            //dataAdapter.
        }

        private void parseCategories(String result) {
            try {
                JSONArray response = new JSONArray(result);
                categorieTable.clear();
                //categorieTable.put()

                for(int i =0;i<response.length();i++)
                {
                    JSONObject object = response.getJSONObject(i);

                    categorieTable.put(object.getString("nom"),object.getString("idcategorie"));
                    categories.add(object.getString("nom"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class UploadAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {

        }



        @Override
        protected Void doInBackground(String... params) {
            try{
                saveRequest(params[0]);
            }
            catch (Exception e)
            {
                Log.d("Erreur",e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void Result ) {

            //spinner.setAdapter(dataAdapter);
            Toast.makeText(ModifierUnProduit.this, "Save",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), home.class);
            startActivity(intent);

            //finishActivity(0);

        }
    }


    private String saveRequest(String requestUrl) throws IOException {
        JSONObject productDetails = new JSONObject();
        try{

            productDetails.put("iddistributeur", LoggedInUser.getId());
            productDetails.put("nom", tv_nomProduit.getText());
            productDetails.put("prixfinal", tv_prixfinal.getText());
            productDetails.put("prixinitial", tv_prixinitial.getText());
            productDetails.put("dlc", tv_dlc.getText());
            productDetails.put("quantite", tv_quantite.getText());
            productDetails.put("categorie", categorieTable.get(spinner.getSelectedItem().toString()));

            BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            String b = Base64.encodeToString(bitmapdata, Base64.NO_WRAP);
            productDetails.put("image",b);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("Erreur",e.getLocalizedMessage()) ;
        }
        String JsonResponse = null;
        String JsonDATA = String.valueOf(productDetails);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String requeteUrl;
        try {
            if(action.equals("Modifier"))
            {
                requeteUrl = "https://dlcapi.herokuapp.com/api/Produits/"+idProduit;

            }
            else
            {
                requeteUrl ="https://dlcapi.herokuapp.com/api/Produits";

            }
            URL url = new URL(requeteUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            //definit le type de requete (POST/GET)
            urlConnection.setRequestMethod("PUT");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            //"ecrit" le json dans les header de la requete
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(JsonDATA);
            writer.close();
            urlConnection.connect();
            int response = urlConnection.getResponseCode(); // recupere le code de reponse suite a la requete effectuee
            String resultheader = Integer.toString(response) ;
            InputStream inputStream ;
            if (response != 200) // si l utlisateur est authentifie
            {
                // recupere la reponse du server en l occurence le json definissant le token, le user id,...
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    jsonResponse.append(line);
                }
                parseResult(jsonResponse.toString()); // parse le json

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
            JSONObject error = response.getJSONObject("error");
            String name=error.getString("name");
            String status=error.getString("message");

            //LoggedInUser.id=response.getString("userId");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private boolean validation() {
        boolean validationPass = true;
        //TextInputLayout txt_prixInitial,txt_prixFinal,txt_Quantite;
        if (tv_prixinitial.getText().toString().trim().isEmpty()) {
            txt_prixInitial.setError("Veuillez renseigner le prix initial du produit");
            validationPass = false;
        }

        if (tv_prixfinal.getText().toString().trim().isEmpty()) {
            txt_prixFinal.setError("Veuillez renseigner le prix final du produit");
            validationPass = false;
        }

        if (tv_quantite.getText().toString().trim().isEmpty()) {
            txt_Quantite.setError("Veuillez renseigner la quantite disponible");
            validationPass = false;
        }

        if(!tv_prixinitial.getText().toString().trim().isEmpty() && !tv_prixfinal.getText().toString().trim().isEmpty())
        {
            if(Double.parseDouble(tv_prixfinal.getText().toString())>Double.parseDouble(tv_prixinitial.getText().toString())){
                txt_prixFinal.setError("Doit etre superieur au prix initial");
                validationPass = false;
            }
        }

        if(tv_nomProduit.getText().toString().trim().isEmpty()){
            txt_nomProduit.setError("Veuillez renseigner un nom pour le produit");
            validationPass = false;
        }
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df.format(Calendar.getInstance().getTime());

        try{
            Date dlc = df.parse(tv_dlc.getText().toString());
            Date DcurrentDate = df.parse(currentDate);

            if (dlc.compareTo(DcurrentDate)<0)
            {
                txt_dlc.setError("Produit deja expire");
                validationPass = false;
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }



        return validationPass;
    }

}

