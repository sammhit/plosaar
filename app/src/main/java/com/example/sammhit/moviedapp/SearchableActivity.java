package com.example.sammhit.moviedapp;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SearchableActivity extends AppCompatActivity {
    public final static String LOG_TAG = SearchableActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        final Intent queryIntent=getIntent();
        handleIntent(queryIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(intent);
        }else if (Intent.ACTION_VIEW.equals(intent.getAction())){
            doView(intent);
        }else{
            Log.i(LOG_TAG,"intent not for search");
        }
    }

    private void doView(Intent intent) {
    }

    private void doMySearch(Intent queryIntent) {
        String queryString = queryIntent.getDataString();
        if (queryString==null){
            queryString=queryIntent.getStringExtra(SearchManager.QUERY);
        }
        Log.i(LOG_TAG,queryString);

    }

}
