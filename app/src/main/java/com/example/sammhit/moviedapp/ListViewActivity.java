package com.example.sammhit.moviedapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.sammhit.moviedapp.data.MovieContract;
import com.example.sammhit.moviedapp.data.MoviesDbHelper;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    RecyclerView recyclerView;
    Cursor cursor;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MoviesDbHelper moviesDbHelper = new MoviesDbHelper(getApplicationContext());
        mDb = moviesDbHelper.getWritableDatabase();
        cursor = getAllMovies();

        MovieListViewAdapter movieListViewAdapter = new MovieListViewAdapter(ListViewActivity.this,cursor);
        recyclerView.setAdapter(movieListViewAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewActivity.this,PlotActivity.class);
                intent.putExtra("Search","ListActivity");
                startActivity(intent);

            }
        });

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
