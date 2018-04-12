package movys.movysapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import movys.movysapp.data.MovieContract;
import movys.movysapp.data.MoviesDbHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;
import java.util.Collections;

public class ListViewActivity extends AppCompatActivity {

    private SQLiteDatabase mDb;
    RecyclerView recyclerView;
    Cursor cursor;
    FloatingActionButton fab;
    Integer[] arr = new Integer[100];
    private AdView mAdView;


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
        getRandArr();

        MovieListViewAdapter movieListViewAdapter = new MovieListViewAdapter(ListViewActivity.this,cursor,arr);
        recyclerView.setAdapter(movieListViewAdapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListViewActivity.this,PlotActivity.class);
                intent.putExtra("Search","ListActivity");
                startActivity(intent);

            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest madRequest = new AdRequest.Builder().build();
        mAdView.loadAd(madRequest);
    }

    private Cursor getAllMovies() {
        return mDb.query(MovieContract.MoviesEntry.TABLE_NAME,null,
                null,
                null,
                null,
                null,
                null);
    }

    public void getRandArr() {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Collections.shuffle(Arrays.asList(arr));

    }
    public void onResume() {
        super.onResume();
        if (mAdView!= null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView!= null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
