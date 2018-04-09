package com.example.sammhit.moviedapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class PlotActivity extends AppCompatActivity {
    public final static String LOG_TAG = PlotActivity.class.getSimpleName();
    public static final int TEXT_REQUEST = 1;
    public List<String> suggest;
    public ArrayAdapter<String> aAdapter;
    View view;
    ProgressBar progressBar;
    public ListView listView;
    TextView plotTextView;
    TextView basicTextView;
    ScrollView scrollView;
    RadioGroup radioGroup;
    RadioButton radioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        suggest = new ArrayList<String>();
        plotTextView = findViewById(R.id.plotTextView);
        view = findViewById(R.id.plotTextView);
        progressBar = findViewById(R.id.progressBar);
        listView =findViewById(android.R.id.list);
        basicTextView=findViewById(R.id.basicTextView);
        scrollView= findViewById(R.id.scrollView);
        listView.bringToFront();
        listView.setAlpha(1);
        listView.setBackgroundColor(Color.WHITE);
        radioGroup=findViewById(R.id.radioButtonGroup);

    }

    private class ExtractActivity extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
             ArrayList<String> extractValue = com.example.sammhit.moviedapp.utils.QueryUtils.extractPlot(strings[0]);
            return extractValue;

        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            //Log.i(LOG_TAG,s);
            progressBar.setVisibility(View.INVISIBLE);
            if (s.size()!=2){
                scrollView.setVisibility(GONE);
                basicTextView.setVisibility(View.VISIBLE);
                basicTextView.setText(s.get(0));
            }
            else {
                String heading = "<h2>"+ s.get(0) +"</h2>";
                String plot ="<p>"+s.get(1)+"</p>";
                basicTextView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Log.i(LOG_TAG, String.valueOf(plotTextView.getVisibility()));
                plotTextView.setText(Html.fromHtml(heading+plot));
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class GetJson extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String newText = strings[0];
            String category = strings[1];
            newText.trim();
            newText.replace(" ", "_");
            suggest = com.example.sammhit.moviedapp.utils.QueryUtils.dynamicSuggests(newText,category);

            runOnUiThread(new Runnable() {
                public void run() {
                    aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, suggest);
                    listView.setAdapter(aAdapter);
                    if (aAdapter.isEmpty()){
                        listView.setVisibility(View.GONE);
                    }
                    else{
                        listView.setVisibility(View.VISIBLE);
                    }
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
        final SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    scrollView.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                new ExtractActivity().execute(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listView.setVisibility(View.VISIBLE);
                //scrollView.setVisibility(GONE);

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                new GetJson().execute(s,radioButton.getText().toString());
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String plotTitle = (String) adapterView.getItemAtPosition(i);
                Log.i("Clicked", plotTitle);
                listView.setVisibility(GONE);
                radioGroup.setVisibility(GONE);
                progressBar.setVisibility(View.VISIBLE);
                new ExtractActivity().execute(plotTitle);
                searchView.clearFocus();


            }
        });


        return true;
    }
}
