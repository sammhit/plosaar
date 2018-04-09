package com.example.sammhit.moviedapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlotActivity extends AppCompatActivity {
    public final static String LOG_TAG = PlotActivity.class.getSimpleName();
    public static final int TEXT_REQUEST = 1;
    public AutoCompleteTextView searchTextView;
    public List<String> suggest;
    public ArrayAdapter<String> aAdapter;
    View view;
    ImageButton searchButton;
    ProgressBar progressBar;
    public ListView listView;


    TextView plotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        suggest = new ArrayList<String>();
        searchTextView = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        plotTextView = findViewById(R.id.plotTextView);
        view = findViewById(R.id.plotTextView);
        progressBar = findViewById(R.id.progressBar);
        listView =findViewById(android.R.id.list);



        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString();
                new GetJson().execute(newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String plotTitle = (String) adapterView.getItemAtPosition(i);
                Log.i("Clicked", plotTitle);
                progressBar.setVisibility(View.VISIBLE);
                new ExtractActivity().execute(plotTitle);

            }
        });

    }

    private class ExtractActivity extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String extractValue = com.example.sammhit.moviedapp.utils.QueryUtils.extractPlot(strings[0]);
            return extractValue;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i(LOG_TAG,s);
            progressBar.setVisibility(View.INVISIBLE);
            plotTextView.setText(s);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void doMysearch(View view) {
        new ExtractActivity().execute(searchTextView.getEditableText().toString());
    }

    private class GetJson extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String newText = strings[0];
            newText.trim();
            newText.replace(" ", "_");
            suggest = com.example.sammhit.moviedapp.utils.QueryUtils.dynamicSuggests(newText);

            runOnUiThread(new Runnable() {
                public void run() {
                    aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, suggest);
                    listView.setAdapter(aAdapter);

                    aAdapter.notifyDataSetChanged();
                }
            });
            return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new ExtractActivity().execute(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                new GetJson().execute(s);
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String plotTitle = (String) adapterView.getItemAtPosition(i);
                Log.i("Clicked", plotTitle);
                progressBar.setVisibility(View.VISIBLE);
                new ExtractActivity().execute(plotTitle);
                listView.setVisibility(View.INVISIBLE);

            }
        });


        return true;
    }
}
