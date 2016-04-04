package com.app.dlc.dlc.fragment.utilisateur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.adapter.ProduitRecycleViewAdapter;
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
 * Created by Guillaume on 31/03/2016.
 */
public class Fragment_ProduitsList extends Fragment {
    private TextView textView;
    private ProgressDialog dialog;
    private static final String TAG = "RecyclerViewExample";
    private List<Produit> produitList = new ArrayList<Produit>();
    private ProduitRecycleViewAdapter adapter;
    private RecyclerView mRecyclerView;





    public Fragment_ProduitsList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        View rootView = inflater.inflate(R.layout.fragment_produitlist, container, false);
        dialog = new ProgressDialog(getActivity());
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait...");

        //textView=(TextView)rootView.findViewById(R.id.detail);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        String token = getArguments().getString("token");
//        textView.setText(token);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Inflate the layout for this fragment
        final String url = "https://dlcapi.herokuapp.com/api/Produits";
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

    public void setTextViewText(String value){
        textView=(TextView)getView().findViewById(R.id.label);
        textView.setText(value);
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
                adapter = new ProduitRecycleViewAdapter(getActivity(), produitList);
                mRecyclerView.setAdapter(adapter);
            } else {
                Log.e(TAG, "Failed to fetch data!");
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONArray response = new JSONArray(result);
            if (null == produitList) {
                produitList = new ArrayList<Produit>();
            }

            for(int i =0;i<response.length();i++)
            {
                JSONObject object = response.getJSONObject(i);
                Produit produit = new Produit();
                produit.setNom(object.getString("nom"));
                produit.setCategorie(object.getString("categorie"));
                produit.setDlc(object.getString("dlc"));
                produit.setQuantite(object.getString("quantite"));
                produit.setPrixFinal(Double.parseDouble(object.getString("prixfinal")));
                produitList.add(produit);
            }


            /*Initialize array if null*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
