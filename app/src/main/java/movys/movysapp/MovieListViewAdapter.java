package movys.movysapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import movys.movysapp.data.MovieContract;


public class MovieListViewAdapter extends RecyclerView.Adapter<MovieListViewAdapter.MovieViewHolder> {
    Context context;
    private Cursor mCursor;
    Integer[] arr = new Integer[100];


    public MovieListViewAdapter(Context context, Cursor cursor,Integer[] arr) {
        this.context = context;
        this.mCursor = cursor;
        this.arr =arr;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlayout, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(arr[position])) {
            return;
        }

        final String movieTitleName = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.TITLE));
        String movieGenre = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.CATEGORIES));
        String movieRating = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.IMDB_RATING));
        String moviePlot = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.PLOT));
        String imageLink = mCursor.getString(mCursor.getColumnIndex(MovieContract.MoviesEntry.LINK));

        final long id = mCursor.getLong(mCursor.getColumnIndex(MovieContract.MoviesEntry._ID));
        Glide.with(context).load(Uri.parse(imageLink)).into(holder.posterView);
        holder.movieTitleView.setText(movieTitleName);
        holder.plotSummaryView.setText(moviePlot);
        holder.genreView.setText(movieGenre);
        holder.ratingView.setText("IMDB:"+movieRating+"/10");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlotActivity.class);
                intent.putExtra("chosenId", id); // put image data in Intent
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
}

