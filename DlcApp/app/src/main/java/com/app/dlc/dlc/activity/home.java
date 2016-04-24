package com.app.dlc.dlc.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.fragment.FragmentDrawer;
import com.app.dlc.dlc.fragment.FriendsFragment;
import com.app.dlc.dlc.fragment.MessagesFragment;
import com.app.dlc.dlc.fragment.distributeur.Fragment_AjouterUnProduit;
import com.app.dlc.dlc.fragment.distributeur.Fragment_MesProduits;
import com.app.dlc.dlc.fragment.distributeur.Fragment_ModifierMesInformations;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_DistributeurList;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_Preference;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_ProduitsList;
import com.app.dlc.dlc.model.InfosDistributeur;
import com.app.dlc.dlc.model.LoggedInUser;
import com.app.dlc.dlc.preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView ed;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private ProgressDialog dialog;
    String nom;
    String voie;
    String ville;
    String zip;
    String horaireOuverture;
    String horaireFermerture;
    byte[] imageAsBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");


        mToolbar = (Toolbar) findViewById(R.id.toolbar); // reference de la toolbar definit dans le layout de l activite

        setSupportActionBar(mToolbar); // "active le support de la toolbar qui n est rien d autre que l action bar
        //getSupportActionBar().setDisplayShowHomeEnabled(true); //definition du bouton a l extreme gauche de l action bar

        //reference du drawer layout
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //reference du navigation view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view
        if(LoggedInUser.type.equals("utilisateur")){
            nvDrawer.inflateMenu(R.menu.drawer_view_utlisateur);
        }
        else{
            nvDrawer.inflateMenu(R.menu.drawer_view_distributeur);
        }


        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();



        // Tie DrawerLayout events to the ActionBarToggle

        mDrawer.addDrawerListener(drawerToggle);



        //reference du drawer (menu de gauche) et l assigne a la toolbar
        /*drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);*/

        if(LoggedInUser.type.equals("utilisateur"))
        {
            // si c est un utlisateur utliser le fragment qui lui est dedie
            Fragment_ProduitsList fragment = null;
            fragment = new Fragment_ProduitsList();
            /*Bundle b = new Bundle();
            b.putString("token", token);
            fragment.setArguments(b);*/
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else {

            // sinon utliser le fragment dedie au distributeur
            Fragment_MesProduits fragment = null;
            fragment = new Fragment_MesProduits();
            /*Bundle b = new Bundle();
            b.putString("token", token);
            fragment.setArguments(b);*/
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open,  R.string.drawer_close);

    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(

                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override

                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        selectDrawerItem(menuItem);

                        return true;

                    }

                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 /*   @Override
    // evenement qui se declenche lorsque l on clique sur le drawer
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }
*/


    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);

        // definit le fragment a instancier et a afficher dans le container_body du layout de l activite home
        if(LoggedInUser.getType().equals("utilisateur"))
        {
            switch (position) {
                case 0:
                    fragment = new Fragment_ProduitsList();
                    title = getString(R.string.title_home);
                    break;
                case 1:
                    fragment = new Fragment_DistributeurList();
                    title = "Liste des distributeurs";
                    break;
                case 2:
                    fragment = new Fragment_Preference();
                    title = "Preferences";
                    break;
                default:
                    break;
            }
        }
        else{ // si c est un distributeur
            switch (position) {
                case 0:
                    fragment = new Fragment_AjouterUnProduit();
                    title = "Ajouter un produit";
                    break;
                case 1:
                    fragment = new Fragment_MesProduits();
                    title = "Mes produits";
                    break;
                case 2:
                    fragment = new Fragment_ModifierMesInformations();
                    title = "Modifier mes informations";
                    break;
                default:
                    break;
            }

        }


        if (fragment != null) {

            //syntaxe pour afficher un fragment dans la zone specifee en lmoccurence container_body
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            //definit le titre a afficher dans l action bar

            getSupportActionBar().setTitle(title);
        }
    }

    @Override

    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        drawerToggle.syncState();

    }
    @Override

    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        // Pass any configuration change to the drawer toggles

        drawerToggle.onConfigurationChanged(newConfig);

    }

    public void selectDrawerItem(MenuItem menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked

        Fragment fragment = null;
        Boolean isFragment = true;

        Class fragmentClass;

        switch(menuItem.getItemId()) {

            case R.id.nav_liste_de_produits:

                fragmentClass = Fragment_ProduitsList.class;

                break;

            case R.id.nav_liste_des_distributeurs:

                fragmentClass = Fragment_DistributeurList.class;

                break;

            case R.id.nav_deconnexion:

                fragmentClass = Fragment_Preference.class;

                break;


            case R.id.nav_mes_produits:

                fragmentClass = Fragment_MesProduits.class;

                break;

            case R.id.nav_modifier_mes_informations:

                fragmentClass = Fragment_Preference.class;
                //Intent intent = new Intent(getApplicationContext(), signup.class);

                Intent intent = new Intent(getApplicationContext(), preferences.class);
                intent.putExtra("Nom", InfosDistributeur.getNom());
                intent.putExtra("Id", InfosDistributeur.getId());
                intent.putExtra("voie", InfosDistributeur.getVoie());
                intent.putExtra("ville", InfosDistributeur.getVille());
                intent.putExtra("codepostal", InfosDistributeur.getCodePostal());
                intent.putExtra("horaireOuverture", InfosDistributeur.getHoraireOuverture());
                intent.putExtra("horaireFermerture", InfosDistributeur.getHoraireFermerture());
                byte[] imageAsBytes = Base64.decode(InfosDistributeur.getImage().getBytes(), Base64.DEFAULT);
                intent.putExtra("Image", imageAsBytes);
                startActivity(intent);
                isFragment=false;
                break;

            case R.id.nav_me_deconnecter:

                fragmentClass = Fragment_Preference.class;

                break;

            default:

                fragmentClass = Fragment_ProduitsList.class;

        }

        if(isFragment){
            try {

                fragment = (Fragment) fragmentClass.newInstance();

            } catch (Exception e) {

                e.printStackTrace();

            }



            // Insert the fragment by replacing any existing fragment

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();



            // Highlight the selected item has been done by NavigationView

            menuItem.setChecked(true);

            // Set action bar title

            setTitle(menuItem.getTitle());

        }





        // Close the navigation drawer

        mDrawer.closeDrawers();

    }








}
