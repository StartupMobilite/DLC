package com.app.dlc.dlc.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.model.Produit;
//import com.app.dlc.dlc.template.ProduitRowTemplate;

import java.util.List;

/**
 * Created by Guillaume on 03/04/2016.
 */
/*
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
    public ProduitRowTemplate onCreateViewHolder(final ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.produitrowtemplate, null);
        cardView=(CardView)parent.findViewById(R.id.card_view);
        ProduitRowTemplate produitRowTemplate = new ProduitRowTemplate(v);
        return produitRowTemplate;
    }

    @Override
    public void onBindViewHolder(ProduitRowTemplate produitRowTemplate, int position) {
        Produit produit = produitList.get(position);
        produitRowTemplate.tv_nom.setText(produit.getNom());
        produitRowTemplate.tv_prixfinal.setText(Double.toString(produit.getPrixFinal()));
        produitRowTemplate.tv_quantite.setText(produit.getQuantite());
        produitRowTemplate.tv_dlc.setText(produit.getDlc());
        produitRowTemplate.tv_categorie.setText(produit.getCategorie());

    }

    @Override
    public int getItemCount() {
        return (null != produitList ? produitList.size() : 0);    }
}
*/
