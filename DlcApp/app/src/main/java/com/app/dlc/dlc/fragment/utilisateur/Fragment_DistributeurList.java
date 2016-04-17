package com.app.dlc.dlc.fragment.utilisateur;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.fragment.distributeur.Fragment_MesProduits;
import com.app.dlc.dlc.model.Distributeur;
import com.app.dlc.dlc.model.LoggedInUser;
import com.app.dlc.dlc.model.Produit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_DistributeurList extends Fragment implements SearchView.OnQueryTextListener {
    private ProgressDialog dialog;
    private static final String TAG = "RecyclerViewExample";
    private List<Distributeur> distributeurList = new ArrayList<Distributeur>();
    private DistributeurRecycleViewAdapter adapter;
    private RecyclerView mRecyclerView;


    public Fragment_DistributeurList() {
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


        // fragment_distributeurlist est le container de base ou s affichera la liste des distributeurs
        View rootView = inflater.inflate(R.layout.fragment_distributeurlist, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        //reference au recycleview declare dans le fichier fragment_distributeurlist
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        // definiton du type d affichage des elements du recycleview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        registerForContextMenu(rootView);


        final String url = "https://dlcapi.herokuapp.com/api/distributeurs?access_token="+ LoggedInUser.token;
        new AsyncHttpTask().execute(url);
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                Log.d(TAG, e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {

            dialog.dismiss();
            /* Download complete. Lets update UI */
            if (result == 1) {
                adapter = new DistributeurRecycleViewAdapter(getActivity(), distributeurList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }
        }
        private void parseResult(String result) {
            try {
                JSONArray response = new JSONArray(result);
                if (null == distributeurList) {
                    distributeurList = new ArrayList<Distributeur>();
                }

                for(int i =0;i<response.length();i++)
                {
                    JSONObject object = response.getJSONObject(i);
                    Distributeur distributeur = new Distributeur();
                    distributeur.setNom(object.getString("nom"));
                    distributeur.setId(object.getInt("id"));
                    distributeur.setAddresse(object.getString("voie")+","+object.getString("ville")+","+object.getString("codepostal"));
                    distributeur.setHoraireOuverture(object.getString("horaireOuverture"));
                    distributeur.setHoraireFermerture(object.getString("horaireFermerture"));
                    distributeur.setImage(object.getString("image"));
                    distributeurList.add(distributeur);
                }


            /*Initialize array if null*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        //inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter = new DistributeurRecycleViewAdapter(getActivity(), distributeurList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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

            List<Distributeur> filteredDistributeurList = filterByName(distributeurList,newText);
            adapter = new DistributeurRecycleViewAdapter(getActivity(), filteredDistributeurList);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        return false;
    }


    private List<Distributeur> filterByName(List<Distributeur> distributeurList, String query) {
        List<Distributeur> filteredList =new ArrayList<Distributeur>();
        for (Distributeur distributeur : distributeurList) {
            /*String s =produit.getNom();
            boolean b = s.contains(query);*/
            if (distributeur.getNom().toLowerCase().startsWith(query)) {
                filteredList.add(distributeur);
            }
        }
        return filteredList;

    }




    public class DistributeurRecycleViewAdapter extends RecyclerView.Adapter<DistributeurRowTemplate> {
        private List<Distributeur> distributeurList;
        //private OnItemClickListener listener;
        private CardView cardView;


        private Context mContext;

        public DistributeurRecycleViewAdapter(Context context, List<Distributeur> distributeurList) {
            this.distributeurList = distributeurList;
            this.mContext = context;
        }

        @Override
        public DistributeurRowTemplate onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributeurrowtemplate_test, null);
            //cardView=(CardView)parent.findViewById(R.id.card_view);
            final TextView t = (TextView)parent.findViewById(R.id.tv_nom);
            DistributeurRowTemplate distributeurRowTemplate = new DistributeurRowTemplate(v);
            return distributeurRowTemplate;
        }

        @Override
        public void onBindViewHolder(DistributeurRowTemplate distributeurRowTemplate, int position) {
            Distributeur distributeur = distributeurList.get(position);
            distributeurRowTemplate.tv_id.setId(distributeur.getId());
            distributeurRowTemplate.tv_nom.setText(distributeur.getNom());
            /*distributeurRowTemplate.tv_addresse.setText(distributeur.getAddresse());
            distributeurRowTemplate.tv_horaireOuverture.setText(distributeur.getHoraireOuverture());
            distributeurRowTemplate.tv_horaireOuverture.setText(distributeur.getHoraireFermerture());*/
            if(!distributeur.getImage().equals("null")) {
                byte[] imageAsBytes = Base64.decode(distributeur.getImage().getBytes(), Base64.DEFAULT);
                //Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                //profileImage.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
                //produitRowTemplate.thumbnail.setImageBitmap(Bitmap.createScaledBitmap(b, 400, 400, false));
                distributeurRowTemplate.thumbnail.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
            }
            else
            {
                distributeurRowTemplate.thumbnail.setImageResource(R.drawable.placeholder);
            }

        }

        @Override
        public int getItemCount() {
            return (null != distributeurList ? distributeurList.size() : 0);    }
    }

    public class DistributeurRowTemplate extends RecyclerView.ViewHolder {

        public ImageView thumbnail;
        public TextView tv_id;
        public TextView tv_nom;
        public TextView tv_addresse;
        public TextView tv_horaireOuverture;
        public TextView tv_horaireFermerture;


        public DistributeurRowTemplate(final View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            this.tv_nom = (TextView) itemView.findViewById(R.id.tv_nom);
            /*this.tv_addresse = (TextView) itemView.findViewById(R.id.tv_addresse);
            this.tv_horaireOuverture = (TextView) itemView.findViewById(R.id.tv_horaireOuverture);
            this.tv_horaireFermerture = (TextView) itemView.findViewById(R.id.tv_horaireFermerture);*/
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    //Toast.makeText(itemView.getContext(), Integer.toString(tv_id.getId()), Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(itemView.getContext(), signup.class));
                    Fragment_ProduitsList fragment = null;
                    fragment = new Fragment_ProduitsList();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", tv_id.getId());
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }

    }

}
