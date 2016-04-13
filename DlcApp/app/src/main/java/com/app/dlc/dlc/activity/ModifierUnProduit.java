package com.app.dlc.dlc.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.model.Distributeur;
import com.app.dlc.dlc.model.LoggedInUser;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class ModifierUnProduit extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Button btn;
    EditText nom,tv_prixinitial,tv_prixfinal,tv_dlc,tv_categorie,tv_quantite;
    TextView tv_nomDistributeur,tv_addresseDistributeur;
    ImageView image;
    ExpandableRelativeLayout expandableLayoutInfoDistributeur;
    String idDistributeur,idProduit;
    Distributeur distributeur;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_un_produit);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        distributeur= new Distributeur();

        image = (ImageView) findViewById(R.id.backdrop);

        tv_prixinitial = (EditText) findViewById(R.id.tv_prixinitial);
        tv_prixfinal = (EditText) findViewById(R.id.tv_prixfinal);
        tv_dlc = (EditText) findViewById(R.id.tv_dlc);
        tv_categorie = (EditText) findViewById(R.id.tv_categorie);
        tv_quantite = (EditText) findViewById(R.id.tv_quantite);

        tv_quantite = (EditText) findViewById(R.id.tv_quantite);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String nom = getIntent().getExtras().getString("Nom");
        idDistributeur = getIntent().getExtras().getString("IdDistributeur");
        idProduit = getIntent().getExtras().getString("Id");
        tv_prixinitial.setText(getIntent().getExtras().getString("PrixInitial"));
        tv_prixfinal.setText(getIntent().getExtras().getString("PrixFinal"));
        tv_dlc.setText(getIntent().getExtras().getString("Dlc"));
        tv_categorie.setText(getIntent().getExtras().getString("Categorie"));
        tv_quantite.setText(getIntent().getExtras().getString("Quantite"));

        byte[] imageAsBytes = getIntent().getExtras().getByteArray("Image");
        image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));

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
            Toast.makeText(this, "Save",
                    Toast.LENGTH_LONG).show();
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
}
