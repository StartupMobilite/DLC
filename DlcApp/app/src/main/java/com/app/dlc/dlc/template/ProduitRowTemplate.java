package com.app.dlc.dlc.template;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dlc.dlc.R;


public class ProduitRowTemplate extends RecyclerView.ViewHolder {

    public ImageView thumbnail;
    public TextView tv_nom;
    public TextView tv_prixfinal;
    public TextView tv_quantite;
    public TextView tv_dlc;
    public TextView tv_categorie;

    public ProduitRowTemplate(View itemView) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.tv_nom = (TextView) itemView.findViewById(R.id.tv_nom);
        this.tv_prixfinal = (TextView) itemView.findViewById(R.id.tv_prixfinal);
        this.tv_quantite = (TextView) itemView.findViewById(R.id.tv_quantite);
        this.tv_dlc = (TextView) itemView.findViewById(R.id.tv_dlc);
        this.tv_categorie = (TextView) itemView.findViewById(R.id.tv_categorie);
    }
}
