package com.app.dlc.dlc.fragment.distributeur;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.activity.ModifierUnProduit;
import com.app.dlc.dlc.activity.signup;
import com.app.dlc.dlc.detail;
import com.app.dlc.dlc.model.Categorie;
import com.app.dlc.dlc.model.LoggedInUser;
import com.app.dlc.dlc.model.Produit;

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
import java.util.Hashtable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_MesProduits extends Fragment implements SearchView.OnQueryTextListener   {
    private ProgressDialog dialog;
    private static final String TAG = "RecyclerViewExample";
    private List<Produit> produitList = new ArrayList<Produit>();
    private List<Categorie> categorieList = new ArrayList<Categorie>();
    private ProduitRecycleViewAdapter adapter;
    private RecyclerView mRecyclerView;
    List<String> categories = new ArrayList<String>();
    Spinner spinner;
    TextView tv;
    ArrayAdapter<String> dataAdapter;
    Hashtable categorieTable;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //FloatingActionButton fab;



    public Fragment_MesProduits() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        View rootView = inflater.inflate(R.layout.fragment_produitlist, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Chargement en cours...");
        String url;
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        //fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        tv = (TextView) rootView.findViewById(R.id.tv);
        registerForContextMenu(rootView);
        //spinner.setOnItemSelectedListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);


        // Spinner Drop down elements

        dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                List<Produit> filteredProduitList;
                if(item.equals("Tout")){
                    adapter = new ProduitRecycleViewAdapter(getActivity(), produitList);
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    tv.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                }
                else
                {
                    filteredProduitList = filterByCategory(produitList, item);
                    adapter = new ProduitRecycleViewAdapter(getActivity(), filteredProduitList);
                    mRecyclerView.setAdapter(adapter);
                    tv.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //textView=(TextView)rootView.findViewById(R.id.detail);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //fab.setVisibility(View.INVISIBLE);
  /*      fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB Action goes here
                Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG);

            }
        });*/

            /*fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG);
                }
            });*/

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG);
                Intent intent = new Intent(getContext(), ModifierUnProduit.class);
                intent.putExtra("Action", "Creer");
                intent.putExtra("IdDistributeur", LoggedInUser.getId());
                startActivity(intent);
            }
        });

        url = "https://dlcapi.herokuapp.com/api/Produits?filter[where][iddistributeur]="+LoggedInUser.getId();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //new AsyncHttpTask().execute(url);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1100);
                //new AsyncHttpTask().execute(url);
                //mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        new AsyncHttpTask().execute(url);
        return rootView;
    }


   /* public void fabClicked(View v){
        // write your code here ..
        Toast.makeText(getActivity(),"Test",Toast.LENGTH_LONG);

    }*/
    private List<Produit> filterByCategory(List<Produit> produitList, String categorie) {
        List<Produit> filteredList = new ArrayList<Produit>();
        for (Produit produit : produitList) {
            if (produit.getCategorie().equals(categorie)) {
                filteredList.add(produit);
            }
        }
        return filteredList;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu_main, menu);
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter = new ProduitRecycleViewAdapter(getActivity(), produitList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        tv.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        //notifyDataSetChanged();

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.isEmpty()){
            tv.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
        }
        else {
            List<Produit> filteredProduitList = filterByName(produitList,newText);
            adapter = new ProduitRecycleViewAdapter(getActivity(), filteredProduitList);
            mRecyclerView.setAdapter(adapter);
            tv.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
        }



        return false;
    }

    private List<Produit> filterByName(List<Produit> produitList, String query) {
        List<Produit> filteredList =new ArrayList<Produit>();
        for (Produit produit : produitList) {
            /*String s =produit.getNom();
            boolean b = s.contains(query);*/
            if (produit.getNom().toLowerCase().startsWith(query)) {
                filteredList.add(produit);
            }
        }
        return filteredList;

    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;
            URL url;

            try {
                url = new URL("https://dlcapi.herokuapp.com/api/Categories?access_token=" + LoggedInUser.getToken());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    fillCategorieHashMap(response.toString());
                }
                /* forming th java.net.URL object */
                url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                statusCode = urlConnection.getResponseCode();

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
                    /*url = new URL("https://dlcapi.herokuapp.com/api/Categories?access_token="+ LoggedInUser.getToken());

                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");

                    statusCode = urlConnection.getResponseCode();
                    if(statusCode ==200){
                        r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        response = new StringBuilder();
                        while ((line = r.readLine()) != null) {
                            response.append(line);
                        }
                        parseCategories(response.toString());
                    }*/

                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            dialog.dismiss();
            /* Download complete. Lets update UI */
            if (result == 1) {
                adapter = new ProduitRecycleViewAdapter(getActivity(), produitList);
                mRecyclerView.setAdapter(adapter);
                spinner.setAdapter(dataAdapter);
                tv.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }
        }

        private void parseResult(String result) {
            try {
                JSONArray response = new JSONArray(result);
                if (null == produitList) {
                    produitList = new ArrayList<Produit>();
                }
                else{
                    produitList.clear();
                }

                dataAdapter.clear();
                dataAdapter.add("Tout");

                for(int i =0;i<response.length();i++)
                {
                    JSONObject object = response.getJSONObject(i);
                    Produit produit = new Produit();
                    produit.setNom(object.getString("nom"));
                    produit.setId(object.getString("idproduit"));
                    produit.setCategorie(categorieTable.get(object.getString("categorie")).toString());
                    if (dataAdapter.getPosition(produit.getCategorie()) == -1) {
                        dataAdapter.add(produit.getCategorie());
                    }
                    produit.setDlc(object.getString("dlc"));
                    produit.setQuantite(object.getString("quantite"));
                    produit.setPrixFinal(Double.parseDouble(object.getString("prixfinal")));
                    produit.setPrixInitial(Double.parseDouble(object.getString("prixinitial")));
                    produit.setIdDistributeur(object.getString("iddistributeur"));
                    if(!object.getString("image").isEmpty())
                        produit.setImage(object.getString("image"));
                    produitList.add(produit);
                }


            /*Initialize array if null*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void fillCategorieHashMap(String result) {
            try {
                JSONArray response = new JSONArray(result);
                categorieTable = new Hashtable();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject object = response.getJSONObject(i);

                    categorieTable.put(object.getString("idcategorie"), object.getString("nom"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void parseCategories(String result) {
            dataAdapter.clear();
            dataAdapter.add("Tout");
            try {
                JSONArray response = new JSONArray(result);


                for(int i =0;i<response.length();i++)
                {
                    JSONObject object = response.getJSONObject(i);
                    for (Categorie categorie : categorieList) {
                        if(categorie.getId()==Integer.parseInt(object.getString("idcategorie"))){
                            categorie.setNom(object.getString("nom"));
                            dataAdapter.add(categorie.getNom());
                            break;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DeleteAsyncTask extends AsyncTask<String, Void,Integer>{

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;
            URL url;

            try {
                url = new URL("https://dlcapi.herokuapp.com/api/Produits/"+params[0]+"?access_token=" + LoggedInUser.getToken());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("DELETE");
                int statusCode = urlConnection.getResponseCode();
                if (statusCode == 200) {

                    return 200;
                }
                else{
                    return 0;
                }
            }

             catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
                 return 0;
            }


        }

        @Override
        protected void onPostExecute(Integer result) {


            /* Download complete. Lets update UI */
            if (result == 200) {
                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT);


            } else {
                Log.e(TAG, "Failed to fetch data!");
                Toast.makeText(getActivity(),"An error has occured",Toast.LENGTH_SHORT);
            }
        }

    }

    protected void deleteProduct(String produitId){
        for(int i=0;i<produitList.size();i++){
                if(produitList.get(i).getId().equals(produitId)){
                    produitList.remove(i);
                    break;
                }
        }
        adapter = new ProduitRecycleViewAdapter(getActivity(), produitList);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }





    public class ProduitRecycleViewAdapter extends RecyclerView.Adapter<ProduitRowTemplate> {
        private List<Produit> produitList;
        //private OnItemClickListener listener;
        private CardView cardView;


        private Context mContext;

        public ProduitRecycleViewAdapter(Context context, List<Produit> produitList) {
            this.produitList = produitList;
            this.mContext = context;
        }

        @Override
        public ProduitRowTemplate onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributeur_produit_row_template, null);
            cardView=(CardView)parent.findViewById(R.id.card_view);
            final TextView t = (TextView)parent.findViewById(R.id.tv_nom);
            ProduitRowTemplate produitRowTemplate = new ProduitRowTemplate(v);
            return produitRowTemplate;
        }

        @Override
        public void onBindViewHolder(ProduitRowTemplate produitRowTemplate, int position) {
            Produit produit = produitList.get(position);
            produitRowTemplate.tv_nom.setText(produit.getNom());
            produitRowTemplate.tv_id.setText(produit.getId());
            produitRowTemplate.tv_prixfinal.setText(Double.toString(produit.getPrixFinal()));
            produitRowTemplate.tv_prixinitial.setText(Double.toString(produit.getPrixInitial()));
            produitRowTemplate.tv_quantite.setText(produit.getQuantite());
            produitRowTemplate.tv_dlc.setText(produit.getDlc());
            produitRowTemplate.tv_categorie.setText(produit.getCategorie());
            produitRowTemplate.idDistributeur=produit.getIdDistributeur();
            if(!produit.getImage().equals("null")) {
                byte[] imageAsBytes = Base64.decode(produit.getImage().getBytes(), Base64.DEFAULT);
                //Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                //profileImage.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
                //produitRowTemplate.thumbnail.setImageBitmap(Bitmap.createScaledBitmap(b, 400, 400, false));
                produitRowTemplate.thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            }
            else
            {
                produitRowTemplate.thumbnail.setImageResource(R.drawable.placeholder);
            }

        }

        @Override
        public int getItemCount() {
            return (null != produitList ? produitList.size() : 0);    }
    }

    public class ProduitRowTemplate extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public CardView card_view;
        public ImageView icon;
        public TextView tv_nom;
        public TextView tv_id;
        public TextView tv_prixfinal;
        public TextView tv_prixinitial;
        public TextView tv_quantite;
        public TextView tv_dlc;
        public TextView tv_categorie;
        public String idDistributeur;


        public ProduitRowTemplate(final View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.card_view = (CardView) itemView.findViewById(R.id.card_view);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            this.tv_nom = (TextView) itemView.findViewById(R.id.tv_nom);
            this.tv_prixfinal = (TextView) itemView.findViewById(R.id.tv_prixfinal);
            this.tv_prixinitial = (TextView) itemView.findViewById(R.id.tv_prixinitial);
            tv_prixinitial.setPaintFlags(tv_prixinitial.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            this.tv_quantite = (TextView) itemView.findViewById(R.id.tv_quantite);
            this.tv_dlc = (TextView) itemView.findViewById(R.id.tv_dlc);
            this.tv_categorie = (TextView) itemView.findViewById(R.id.tv_categorie);

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_modifier:
                                    Intent intent = new Intent(itemView.getContext(), ModifierUnProduit.class);
                                    intent.putExtra("Action", "Modifier");
                                    intent.putExtra("IdDistributeur", idDistributeur);
                                    intent.putExtra("Nom", tv_nom.getText());
                                    intent.putExtra("Id", tv_id.getText());
                                    intent.putExtra("PrixInitial", tv_prixinitial.getText());
                                    intent.putExtra("PrixFinal", tv_prixfinal.getText());
                                    intent.putExtra("Dlc", tv_dlc.getText());
                                    intent.putExtra("Categorie", tv_categorie.getText());
                                    intent.putExtra("Quantite", tv_quantite.getText());

                                    BitmapDrawable drawable = (BitmapDrawable) thumbnail.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                    byte[] bitmapdata = stream.toByteArray();
                                    intent.putExtra("Image", bitmapdata);

                                    startActivity(intent);
                                    return true;
                                case R.id.item_supprimer:
                                    //Toast.makeText(itemView.getContext(), "id ="+tv_id.getText(), Toast.LENGTH_SHORT).show();
                                    deleteProduct(tv_id.getText().toString());
                                    DeleteAsyncTask d = new DeleteAsyncTask();
                                    d.execute(tv_id.getText().toString());
                                    return true;
                            }
                            return true;

                        }
                    });
                    popupMenu.inflate(R.menu.menu_popup);
                    popupMenu.show();
                }
            });
            card_view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //Toast.makeText(itemView.getContext(), tv_nom.getText().toString(), Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(itemView.getContext(), detail.class));
                    Intent intent = new Intent(itemView.getContext(), ModifierUnProduit.class);
                    intent.putExtra("Action", "Modifier");
                    intent.putExtra("IdDistributeur", idDistributeur);
                    intent.putExtra("Nom", tv_nom.getText());
                    intent.putExtra("Id", tv_id.getText());
                    intent.putExtra("PrixInitial", tv_prixinitial.getText());
                    intent.putExtra("PrixFinal", tv_prixfinal.getText());
                    intent.putExtra("Dlc", tv_dlc.getText());
                    intent.putExtra("Categorie", tv_categorie.getText());
                    intent.putExtra("Quantite", tv_quantite.getText());

                    BitmapDrawable drawable = (BitmapDrawable) thumbnail.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();
                    intent.putExtra("Image", bitmapdata);

                    startActivity(intent);

                    /*Fragment_MesProduits fragment = null;
                    fragment = new Fragment_MesProduits();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

                }
            });
        }

    }
}


