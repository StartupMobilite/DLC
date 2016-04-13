package com.app.dlc.dlc.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.app.dlc.dlc.model.LoggedInUser;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        mToolbar = (Toolbar) findViewById(R.id.toolbar); // reference de la toolbar definit dans le layout de l activite

        setSupportActionBar(mToolbar); // "active le support de la toolbar qui n est rien d autre que l action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true); //definition du bouton a l extreme gauche de l action bar

        //reference du drawer (menu de gauche) et l assigne a la toolbar
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

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

    @Override
    // evenement qui se declenche lorsque l on clique sur le drawer
    public void onDrawerItemSelected(View view, int position) {

        displayView(position);
    }



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
}
