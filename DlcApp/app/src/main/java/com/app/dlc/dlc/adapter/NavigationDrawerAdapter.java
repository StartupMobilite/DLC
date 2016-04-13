package com.app.dlc.dlc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.dlc.dlc.R;
import com.app.dlc.dlc.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //defininit le layout correspondant a chaque element du menu
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    //cette classe definit le contenu de chaque element de du drawer
    //chaque element du drawer est en fait un rcyclerview
    // le layout ocrrespondant et nav_drawer_row (voir la fonction onCreateViewHolder)
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            //expose le controle qui a l id title de facon a ce qu il puisse etre modifie dynamiquement
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
