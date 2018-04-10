package com.example.sammhit.moviedapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sammhit.moviedapp.data.MovieContract;
import com.example.sammhit.moviedapp.data.MoviesDbHelper;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    RecyclerView recyclerView;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(getApplicationContext());
        mDb = moviesDbHelper.getWritableDatabase();
        cursor = getAllMovies();

        MovieListViewAdapter movieListViewAdapter = new MovieListViewAdapter(ListViewActivity.this,cursor);
        recyclerView.setAdapter(movieListViewAdapter);

    }

    private Cursor getAllMovies() {
        return mDb.query(MovieContract.MoviesEntry.TABLE_NAME,null,
                null,
                null,
                null,
                null,
                null);
    }

}
