package com.app.dlc.dlc;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.dlc.dlc.activity.login;
import com.app.dlc.dlc.activity.signup;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentDistribteur extends Fragment {

    EditText input_email,input_password,input_voie,input_ville,input_zip,tv_horaireFermerture,tv_horaireOuverture;
    TextInputLayout input_layout_email,input_layout_password,input_layout_voie,input_layout_ville,input_layout_zip;
    TimePickerDialog.OnTimeSetListener timeOuverture;
    TimePickerDialog.OnTimeSetListener timeFermerture;
    Calendar myCalendar;
    DateFormat fmtDateAndTime;
    TextView link_login;
    Button btn_signup;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;


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

        link_login = (TextView) rootView.findViewById(R.id.link_login);
        btn_signup = (Button) rootView.findViewById(R.id.btn_signup);

        input_email = (EditText) rootView.findViewById(R.id.input_email);
        input_password = (EditText) rootView.findViewById(R.id.input_password);
        input_voie = (EditText) rootView.findViewById(R.id.input_voie);
        input_ville = (EditText) rootView.findViewById(R.id.input_ville);
        input_zip = (EditText) rootView.findViewById(R.id.input_zip);
        tv_horaireOuverture = (EditText) rootView.findViewById(R.id.tv_horaireOuverture);
        tv_horaireOuverture.setInputType(InputType.TYPE_NULL);

        tv_horaireFermerture = (EditText) rootView.findViewById(R.id.tv_horaireFermerture);
        tv_horaireFermerture.setInputType(InputType.TYPE_NULL);


        input_layout_email = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        input_layout_password = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
        input_layout_voie = (TextInputLayout) rootView.findViewById(R.id.input_layout_voie);
        input_layout_ville = (TextInputLayout) rootView.findViewById(R.id.input_layout_ville);
        input_layout_zip = (TextInputLayout) rootView.findViewById(R.id.input_layout_zip);

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
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
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
                String b = Base64.encodeToString(bitmapdata, Base64.NO_WRAP);
                } else {
                Toast.makeText(getContext(), "Vous n'avez choisi aucune image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
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




}
