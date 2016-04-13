package com.app.dlc.dlc.template;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_Preference;
import com.app.dlc.dlc.fragment.utilisateur.Fragment_ProduitsList;


/*public class ProduitRowTemplate extends RecyclerView.ViewHolder {

    public ImageView thumbnail;
    public TextView tv_nom;
    public TextView tv_prixfinal;
    public TextView tv_quantite;
    public TextView tv_dlc;
    public TextView tv_categorie;


    public ProduitRowTemplate(final View itemView) {
        super(itemView);
        this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        this.tv_nom = (TextView) itemView.findViewById(R.id.tv_nom);
        this.tv_prixfinal = (TextView) itemView.findViewById(R.id.tv_prixfinal);
        this.tv_quantite = (TextView) itemView.findViewById(R.id.tv_quantite);
        this.tv_dlc = (TextView) itemView.findViewById(R.id.tv_dlc);
        this.tv_categorie = (TextView) itemView.findViewById(R.id.tv_categorie);
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                Toast.makeText(itemView.getContext(), tv_nom.getText().toString(), Toast.LENGTH_SHORT).show();
                *//*Fragment fragment = null;
                fragment = new Fragment_Preference();
                FragmentManager fragmentManager = Activity.class.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();*//*


            }
        });
    }

}*/
