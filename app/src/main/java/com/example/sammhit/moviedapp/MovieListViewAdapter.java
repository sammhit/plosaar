package com.example.sammhit.moviedapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by sammhit on 10/4/18.
 */
public class MovieListViewAdapter extends RecyclerView.Adapter {
    Context context;

    public MovieListViewAdapter(Context context){
        this.context =context;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
