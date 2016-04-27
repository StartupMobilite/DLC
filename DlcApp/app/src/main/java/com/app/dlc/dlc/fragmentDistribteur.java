package com.app.dlc.dlc;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.dlc.dlc.activity.home;
import com.app.dlc.dlc.activity.login;
import com.app.dlc.dlc.activity.signup;
import com.app.dlc.dlc.model.LoggedInUser;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentDistribteur extends Fragment {

    EditText input_name,input_email,input_password,input_voie,input_ville,input_zip,tv_horaireFermerture,tv_horaireOuverture;
    TextInputLayout input_layout_name,input_layout_email,input_layout_password,input_layout_voie,input_layout_ville,input_layout_zip;
    TimePickerDialog.OnTimeSetListener timeOuverture;
    TimePickerDialog.OnTimeSetListener timeFermerture;
    Calendar myCalendar;
    DateFormat fmtDateAndTime;
    TextView link_login;
    Button btn_signup;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    String imageByte;
    private ProgressDialog dialog;
    String requestResult;


    public fragmentDistribteur() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_distribteur, container, false);
        //Calendar dateAndTime=Calendar.getInstance();
        //fmtDateAndTime=DateFormat.getDateTimeInstance();
        fmtDateAndTime=DateFormat.getTimeInstance();

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Creation du compte en cours...");

        link_login = (TextView) rootView.findViewById(R.id.tv_member);
        btn_signup = (Button) rootView.findViewById(R.id.btn_signup);

        input_name = (EditText) rootView.findViewById(R.id.input_name);
        input_email = (EditText) rootView.findViewById(R.id.input_email);
        input_password = (EditText) rootView.findViewById(R.id.input_password);
        input_voie = (EditText) rootView.findViewById(R.id.input_voie);
        input_ville = (EditText) rootView.findViewById(R.id.input_ville);
        input_zip = (EditText) rootView.findViewById(R.id.input_zip);
        tv_horaireOuverture = (EditText) rootView.findViewById(R.id.tv_horaireOuverture);
        tv_horaireOuverture.setInputType(InputType.TYPE_NULL);

        tv_horaireFermerture = (EditText) rootView.findViewById(R.id.tv_horaireFermerture);
        tv_horaireFermerture.setInputType(InputType.TYPE_NULL);


        input_layout_name = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
        input_layout_email = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
        input_layout_voie = (TextInputLayout) rootView.findViewById(R.id.input_layout_voie);
        input_layout_ville = (TextInputLayout) rootView.findViewById(R.id.input_layout_ville);
        input_layout_zip = (TextInputLayout) rootView.findViewById(R.id.input_layout_zip);

        myCalendar = Calendar.getInstance();


        tv_horaireOuverture.setText("9:00");
        tv_horaireFermerture.setText("19:00");




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
                new TimePickerDialog(getActivity(),timeOuverture,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        tv_horaireFermerture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),timeFermerture,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if(validation())
                if(true)
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getContext());

                    // set title
                    alertDialogBuilder.setTitle("Upload");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Voulez-vous uploader votre logo maintenant ? Vous pourrez toujours le definir plus tard")
                            .setCancelable(false)
                            .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    //MainActivity.this.finish();
                                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    // Start the Intent
                                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing

                                    dialog.cancel();
                                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] bitmapdata = stream.toByteArray();
                                    imageByte = Base64.encodeToString(bitmapdata, Base64.NO_WRAP);
                                    SignInAsyncTask sat= new SignInAsyncTask();
                                    sat.execute("ok");
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }



            }
        });



        //return inflater.inflate(R.layout.fragment_distribteur, container, false);
        return rootView;
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
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                //ImageView imgView = (ImageView) findViewById(R.id.backdrop);
                // Set the Image in ImageView after decoding the String

                //getResizedBitmap(BitmapFactory.decodeFile(imgDecodableString), imgView.getHeight(),imgView.getWidth());

                //imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));


                //imgView.setImageBitmap(getResizedBitmap(BitmapFactory.decodeFile(imgDecodableString), imgView.getHeight(),imgView.getWidth()));

                //Bitmap bitmap = getResizedBitmap(BitmapFactory.decodeFile(imgDecodableString), imgView.getHeight(),imgView.getWidth());
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                imageByte = Base64.encodeToString(bitmapdata, Base64.NO_WRAP);
                } else {
                Toast.makeText(getContext(), "Vous n'avez choisi aucune image",
                        Toast.LENGTH_LONG).show();
            }
            SignInAsyncTask sat= new SignInAsyncTask();
            sat.execute("ok");
        } catch (Exception e) {
            Log.d("Erreur",e.getLocalizedMessage());
            Toast.makeText(getContext(), "Une erreur s est produite", Toast.LENGTH_LONG)
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

    private boolean validation() {
        boolean validationPass = true;
        if (TextUtils.isEmpty(input_email.getText()) || !android.util.Patterns.EMAIL_ADDRESS.matcher(input_email.getText()).matches()
        ) {

            input_layout_email.setError("Addresse email incorrecte");
            validationPass = false;
        }
        else {
            input_layout_email.setErrorEnabled(false);
        }

        if (input_name.getText().toString().trim().isEmpty()) {
            input_layout_name.setError("Entrez le nom de votre surface");
            validationPass = false;
        }
        else
        {
            input_layout_name.setErrorEnabled(false);
        }

        if (input_password.getText().toString().trim().isEmpty() || input_password.getText().length()<5) {
            input_layout_password.setError("Entrez un mot de passe d'au moins 5 caracteres");
            validationPass = false;
        }
        else{
            input_layout_password.setErrorEnabled(false);
        }


        if (input_voie.getText().toString().trim().isEmpty()) {
            input_layout_voie.setError("Ce champ ne doit pas etre vide");
            validationPass = false;
        }
        else{
            input_layout_voie.setErrorEnabled(false);
        }
        if (input_ville.getText().toString().trim().isEmpty()) {
            input_layout_ville.setError("Ce champ ne doit pas etre vide");
            validationPass = false;
        }
        else{
            input_layout_ville.setErrorEnabled(false);
        }



        if(input_zip.getText().toString().trim().isEmpty() || input_zip.getText().length()!=5){
            input_layout_zip.setError("Zip code invalide");
            validationPass = false;
        }
        else{
            input_layout_zip.setErrorEnabled(false);
        }
        //return validationPass;
        return true;
    }


    public class SignInAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }



        @Override
        protected String doInBackground(String... params) {

            try{
                signInRequest(params[0]);
                //return "OK";
            }
            catch (Exception e)
            {
                Log.d("Erreur",e.getLocalizedMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result ) {

            //spinner.setAdapter(dataAdapter);
            dialog.dismiss(); //masque le dialg box

            try{
                if(requestResult=="OK")
                {
                    //Toast.makeText(getContext(), "Votre compte a ete cree vous pouvez vous identifer",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), login.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivityForResult(intent, 0); // execution de l intent
                    startActivity(intent);

                }
                else if(requestResult =="Exists")
                {
                    Toast.makeText(getContext(), "Cette addresse est deja utilisee pour un autre compte",Toast.LENGTH_LONG).show();
                    input_layout_email.setError("Adresse email deja utilisee");

                }
                else
                {
                    Toast.makeText(getContext(), "erreur",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), login.class);
                    startActivity(intent);


                }
            }
            catch (Exception e)
            {
                String s = e.getLocalizedMessage();
                Log.d("Erreur",e.getLocalizedMessage());
            }



        }
    }




    private String signInRequest(String requestUrl) throws IOException {
        String s;
        String resultheader="";
        JSONObject productDetails = new JSONObject();
        try {

            productDetails.put("voie", input_voie.getText());
            productDetails.put("ville", input_ville.getText());
            productDetails.put("codepostal", input_zip.getText());
            productDetails.put("horaireOuverture", tv_horaireOuverture.getText());
            productDetails.put("horaireFermerture", tv_horaireFermerture.getText());
            productDetails.put("email", input_email.getText());
            productDetails.put("nom", input_name.getText());
            productDetails.put("password", input_password.getText());
            /*productDetails.put("voie", "20 rue fontarabie");
            productDetails.put("ville", "Paris");
            productDetails.put("codepostal", "75020");
            productDetails.put("horaireOuverture", tv_horaireOuverture.getText());
            productDetails.put("horaireFermerture", tv_horaireFermerture.getText());
            productDetails.put("email", "abc@yxz.com");
            productDetails.put("nom", "ABC");
            productDetails.put("password", "12345");*/
            if(imageByte.toString().length()>0)
                productDetails.put("image", imageByte);

        }
         catch (JSONException e) {
            e.printStackTrace();
        }




    try
    {


        String JsonResponse = null;
        String JsonDATA = String.valueOf(productDetails);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String requeteUrl;


            requeteUrl = "https://dlcapi.herokuapp.com/api/Distributeurs";

            URL url = new URL(requeteUrl);
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

            InputStream inputStream ;
            if (response == 200){
                requestResult=resultheader="OK";
                return resultheader;
            }

            else if(response==422){
                requestResult=resultheader="Exists";
                return resultheader;
            }

            else
            {
                // recupere la reponse du server en l occurence le json definissant le token, le user id,...
                BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    jsonResponse.append(line);
                }
                requestResult=resultheader=parseResult(jsonResponse.toString()); // parse le json

            }

            //return resultheader;
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            String str;
            Log.d("Erreur",e.getLocalizedMessage()) ;
            str=e.getLocalizedMessage().toString();
            str=e.getMessage().toString();
        }

        return resultheader;


}

    private String parseResult(String result) {
        // parsage du json
        try {
            JSONObject response = new JSONObject(result);
            JSONObject error = response.getJSONObject("error");
            String name=error.getString("name");
            String message=error.getString("message");
            return message;

            //LoggedInUser.id=response.getString("userId");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "Error";
    }




}
