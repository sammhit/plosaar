package com.example.sammhit.moviedapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sammhit.moviedapp.data.MovieContract;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sammhit on 10/4/18.
 */
public class MovieListViewAdapter extends RecyclerView.Adapter<MovieListViewAdapter.MovieViewHolder> {
    Context context;
    private Cursor mCursor;

    ArrayList movieTitleNames = new ArrayList<>(Arrays.asList("Fast and Furious 1", "Fast and Furious 2", "Fast and Furious 3", "Fast and Furious 4", "Fast and Furious 5", "Fast and Furious 6", "Fast and Furious 7", "Fast and Furious 8", "Fast and Furious 9", "Fast and Furious 10"));
    ArrayList moviePlotNames = new ArrayList<>(Arrays.asList(
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread",
            "I am Sammhit Mohapatra. My life is still fucked up and I am dreaming of shit I dont know. Everything in my life is going haywire and I am here eating papa's bread"));
    ArrayList movieGenreNames = new ArrayList<>(Arrays.asList(
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery",
            "Genre : Animation, Adventure, Comedy, Crime, Family, Mystery"));
    ArrayList movieRatings = new ArrayList<>(Arrays.asList(
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10",
            "7.8/10"));

    public MovieListViewAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.mCursor = cursor;

    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        if (!mCursor.moveToPosition(position)) {
            return;
        }

        final String movieTitleName = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.TITLE));
        String movieGenre = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.CATEGORIES));
        String movieRating = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.IMDB_RATING));
        String moviePlot = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.PLOT));
        String imageLink = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.LINK));

        long id = mCursor.getLong(mCursor.getColumnIndex(MovieContract.MoviesEntry._ID));
        Glide.with(context).load(Uri.parse(imageLink)).into(holder.posterView);
        holder.movieTitleView.setText(movieTitleName);
        holder.plotSummaryView.setText(moviePlot);
        holder.genreView.setText(movieGenre);
        holder.ratingView.setText("IMDB:"+movieRating+"/10");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlotActivity.class);
                intent.putExtra("chosenTitle", movieTitleName); // put image data in Intent
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterView;
        TextView movieTitleView;
        TextView plotSummaryView;
        TextView genreView;
        TextView ratingView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.posterView);
            movieTitleView = itemView.findViewById(R.id.mainTitleView);
            plotSummaryView = itemView.findViewById(R.id.plotSummaryView);
            genreView = itemView.findViewById(R.id.genreView);
            ratingView = itemView.findViewById(R.id.ratingView);
        }
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

}

