package com.app.dlc.dlc.model;

//import android.app.Fragment;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.fragment.FriendsFragment;
import com.app.dlc.dlc.fragment.MessagesFragment;
import com.app.dlc.dlc.fragment.distributeur.Fragment_AjouterUnProduit;
import com.app.dlc.dlc.fragment.distributeur.Fragment_MesProduits;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_DistributeurList;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_Preference;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_ProduitsList;

public class DrawerTest extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        // Find our drawer view

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Find our drawer view



        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view

        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();



        // Tie DrawerLayout events to the ActionBarToggle

        mDrawer.addDrawerListener(drawerToggle);
    }


    private ActionBarDrawerToggle setupDrawerToggle() {

        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);

    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {

            return true;

        }

        switch (item.getItemId()) {

            case android.R.id.home:

                mDrawer.openDrawer(GravityCompat.START);

                return true;

        }



        return super.onOptionsItemSelected(item);

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



    public void selectDrawerItem(MenuItem menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked

        Fragment fragment = null;

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

            /*case R.id.nav_ajouter_un_produit:

                fragmentClass = Fragment_AjouterUnProduit.class;

                break;*/

            case R.id.nav_mes_produits:

                fragmentClass = Fragment_MesProduits.class;

                break;

            case R.id.nav_modifier_mes_informations:

                fragmentClass = Fragment_Preference.class;

                break;

            case R.id.nav_me_deconnecter:

                fragmentClass = Fragment_Preference.class;

                break;

            default:

                fragmentClass = Fragment_ProduitsList.class;

        }



        try {

            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {

            e.printStackTrace();

        }



        // Insert the fragment by replacing any existing fragment

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();



        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);

        // Set action bar title

        setTitle(menuItem.getTitle());

        // Close the navigation drawer

        mDrawer.closeDrawers();

    }



    // `onPostCreate` called when activity start-up is complete after `onStart()`

    // NOTE! Make sure to override the method with only a single `Bundle` argument

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



}
