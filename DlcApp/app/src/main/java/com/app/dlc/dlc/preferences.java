package com.app.dlc.dlc;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.dlc.dlc.activity.home;
import com.app.dlc.dlc.model.InfosDistributeur;
import com.app.dlc.dlc.model.LoggedInUser;
import com.app.dlc.dlc.model.Produit;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

public class preferences extends AppCompatActivity {
    private ProgressDialog dialog;
    ImageView backdrop;
    TextInputLayout input_layout_nom,input_layout_voie,input_layout_ville,input_layout_zip;
    EditText input_nom,input_voie,input_ville,input_zip,tv_horaireFermerture,tv_horaireOuverture;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Calendar myCalendar;
    TimePickerDialog.OnTimeSetListener timeOuverture;
    TimePickerDialog.OnTimeSetListener timeFermerture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        input_layout_nom=(TextInputLayout)findViewById(R.id.input_layout_nom);
        input_layout_voie=(TextInputLayout)findViewById(R.id.input_layout_voie);
        input_layout_ville=(TextInputLayout)findViewById(R.id.input_layout_ville);
        input_layout_zip=(TextInputLayout)findViewById(R.id.input_layout_zip);

        input_nom=(EditText)findViewById(R.id.input_nom);
        input_voie=(EditText)findViewById(R.id.input_voie);
        input_ville=(EditText)findViewById(R.id.input_ville);
        input_zip=(EditText)findViewById(R.id.input_zip);
        tv_horaireFermerture=(EditText)findViewById(R.id.tv_horaireFermerture);
        tv_horaireOuverture=(EditText)findViewById(R.id.tv_horaireOuverture);
        tv_horaireOuverture.setInputType(InputType.TYPE_NULL);
        tv_horaireFermerture.setInputType(InputType.TYPE_NULL);

        backdrop=(ImageView) findViewById(R.id.backdrop);
        input_nom.setText(getIntent().getExtras().getString("Nom"));
        input_voie.setText(getIntent().getExtras().getString("voie"));
        input_ville.setText(getIntent().getExtras().getString("ville"));
        input_zip.setText(getIntent().getExtras().getString("codepostal"));
        tv_horaireOuverture.setText(getIntent().getExtras().getString("horaireOuverture"));
        tv_horaireFermerture.setText(getIntent().getExtras().getString("horaireFermerture"));
        byte[] imageAsBytes = getIntent().getExtras().getByteArray("Image");
        backdrop.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

        myCalendar = Calendar.getInstance();

        timeOuverture=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabelOuverture();
            }
        };

        timeFermerture=new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                updateLabelFermerture();
            }
        };



        tv_horaireOuverture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(preferences.this,timeOuverture,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        tv_horaireFermerture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(preferences.this,timeFermerture,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
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




    }
    private void updateLabelOuverture() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        //tv_horaireOuverture.setText(fmtDateAndTime.format(myCalendar.getTime()));
        tv_horaireOuverture.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelFermerture() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        //tv_horaireOuverture.setText(fmtDateAndTime.format(myCalendar.getTime()));
        tv_horaireFermerture.setText(sdf.format(myCalendar.getTime()));
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

                //getResizedBitmap(BitmapFactory.decodeFile(imgDecodableString), imgView.getHeight(),imgView.getWidth());

                //imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                imgView.setImageBitmap(getResizedBitmap(BitmapFactory.decodeFile(imgDecodableString), imgView.getHeight(),imgView.getWidth()));



            } else {
                Toast.makeText(this, "Vous n'avez choisi aucune image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Une erreur s est produite", Toast.LENGTH_LONG)
                    .show();
        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

// create a matrix for the manipulation

        Matrix matrix = new Matrix();

// resize the bit map

        matrix.postScale(scaleWidth, scaleHeight);

// recreate the new Bitmap

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

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

                String url ="https://dlcapi.herokuapp.com/api/Distributeurs/"+LoggedInUser.getId()+"?access_token="+LoggedInUser.token;
                UpdateInfoAsyncTask updateInfoAsyncTask =new UpdateInfoAsyncTask();
                updateInfoAsyncTask.execute(url);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class UpdateInfoAsyncTask extends AsyncTask<String, Void, Void> {
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
            Toast.makeText(preferences.this, "Save",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), home.class);
            startActivity(intent);

            //finishActivity(0);

        }
    }



    public void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            for (int i = 0; i < response.length(); i++) {
                JSONObject object = response.getJSONObject(i);
                input_nom.setText(object.getString("nom"));
                input_voie.setText(object.getString("voie"));
                input_ville.setText(object.getString("ville"));
                input_zip.setText(object.getString("codepostal"));
                tv_horaireFermerture.setText(object.getString("horaireOuverture"));
                tv_horaireFermerture.setText(object.getString("horaireFermerture"));

                String image =object.getString("image");
                byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
                backdrop.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String saveRequest(String requestUrl) throws IOException {
        JSONObject productDetails = new JSONObject();
        String b=null;
        try {


            productDetails.put("nom", input_nom.getText());
            productDetails.put("ville", input_ville.getText());
            productDetails.put("voie", input_voie.getText());
            productDetails.put("codepostal", input_zip.getText());
            productDetails.put("horaireOuverture", tv_horaireOuverture.getText());
            productDetails.put("horaireFermerture", tv_horaireFermerture.getText());
            BitmapDrawable drawable = (BitmapDrawable) backdrop.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapdata = stream.toByteArray();
            b = Base64.encodeToString(bitmapdata, Base64.NO_WRAP);
            productDetails.put("image", b);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("Erreur", e.getLocalizedMessage());
        }
        String JsonResponse = null;
        String JsonDATA = String.valueOf(productDetails);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String requeteUrl = "https://dlcapi.herokuapp.com/api/Distributeurs/" + LoggedInUser.getId() + "?access_token=" + LoggedInUser.getToken();

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
        String resultheader = Integer.toString(response);
        InputStream inputStream;
        if (response != 200) // si l utlisateur est authentifie
        {
            // recupere la reponse du server en l occurence le json definissant le token, le user id,...
            BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                jsonResponse.append(line);
            }
            resultheader=parseResultError(jsonResponse.toString()); // parse le json

        }


        InfosDistributeur.setNom(input_nom.getText().toString());
        InfosDistributeur.setVoie( input_voie.getText().toString());
        InfosDistributeur.setVille(input_ville.getText().toString());
        InfosDistributeur.setCodePostal(input_zip.getText().toString());
        InfosDistributeur.setHoraireOuverture(tv_horaireOuverture.getText().toString());
        InfosDistributeur.setHoraireFermerture(tv_horaireFermerture.getText().toString());
        InfosDistributeur.setImage(b);


        return resultheader;

    }

    private String parseResultError(String result) {
        // parsage du json
        try {
            JSONObject response = new JSONObject(result);
            JSONObject error = response.getJSONObject("error");
            String name=error.getString("name");
            String status=error.getString("message");
            return  status;

            //LoggedInUser.id=response.getString("userId");

        } catch (JSONException e) {
            e.printStackTrace();
            return "Erreur";
        }
    }
    private boolean validation() {
        boolean validationPass = true;
        //TextInputLayout txt_prixInitial,txt_prixFinal,txt_Quantite;
        if (input_nom.getText().toString().trim().isEmpty()) {
            input_layout_nom.setError("Veuillez le nom de votre surface");
            validationPass = false;
        }

        if (input_voie.getText().toString().trim().isEmpty()) {
            input_layout_voie.setError("Veuillez renseigner un nom de la rue ou de la voie");
            validationPass = false;
        }

        if (input_ville.getText().toString().trim().isEmpty()) {
            input_layout_ville.setError("Veuillez la ville ou se situe votre surface");
            validationPass = false;
        }


/*
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
        }*/



        return validationPass;
    }

}
