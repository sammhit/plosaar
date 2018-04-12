package movys.movysapp;


import android.app.SearchManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import movys.movysapp.data.MoviesDbHelper;
import movys.movysapp.data.MovieContract.MoviesEntry;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import static android.view.View.GONE;

public class PlotActivity extends AppCompatActivity {
    public final static String LOG_TAG = PlotActivity.class.getSimpleName();
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
    Long idFromListActivity=null;
    String searchActivity=null;
    Cursor mCursor;
    SQLiteDatabase mDb;
    private InterstitialAd interstitialAd=null;
    private AdView mAdView;
    String shit= "\\nIt looks like we don\\''t have a Synopsis for this title yet. Be the first to contribute! Just click the \"Edit page\" button at the bottom of the page or learn more in the Synopsis submission guide.\\n";


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

        MobileAds.initialize(this,"ca-app-pub-4413278369673308~2766249162");
        interstitialAd= new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        mAdView = findViewById(R.id.adView);
        AdRequest madRequest = new AdRequest.Builder().build();
        mAdView.loadAd(madRequest);

        Intent intent = getIntent();
        idFromListActivity=intent.getLongExtra("chosenId",1);
        searchActivity =intent.getStringExtra("Search");

        MoviesDbHelper moviesDbHelper= new MoviesDbHelper(getApplicationContext());
        mDb = moviesDbHelper.getWritableDatabase();
        if (idFromListActivity!=null){
            searchTitleAndDisplay(idFromListActivity);
        }

    }

    private void searchTitleAndDisplay(Long idFromListActivity) {


        mCursor = getDetails(idFromListActivity);
        mCursor.moveToNext()    ;
        String summary = "<p>"+mCursor.getString(mCursor.getColumnIndex(MoviesEntry.PLOTSUMMARY))+"</p>";
        String headingTitle ="<h2>"+ mCursor.getString(mCursor.getColumnIndex(MoviesEntry.TITLE))+"</h2>";
        listView.setVisibility(GONE);
        radioGroup.setVisibility(GONE);
        basicTextView.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        plotTextView.setText(Html.fromHtml(headingTitle+summary));

    }

    private class ExtractActivity extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
             ArrayList<String> extractValue = movys.movysapp.utils.QueryUtils.extractPlot(strings[0]);
            return extractValue;

        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            //Log.i(LOG_TAG,s);
            String plot=null;
            progressBar.setVisibility(View.INVISIBLE);
            if (s.size()!=2){
                scrollView.setVisibility(GONE);
                basicTextView.setVisibility(View.VISIBLE);
                basicTextView.setText(s.get(0));

            }
            else {
                String heading = "<h2>"+ s.get(0) +"</h2>";
                plot ="<p>"+s.get(1)+"</p>";

                String[] whereArgs={shit};
                basicTextView.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                plotTextView.setText(Html.fromHtml(heading+plot));
            }/*
            if (plot!=null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesEntry.PLOTSUMMARY,plot);
                String[] hehe = new String[]{MoviesEntry._ID};
                String ehh = MoviesEntry.PLOTSUMMARY + " LIKE " +"\'"+"%Synopsis%"+"\'";
                Cursor finCursor= mDb.query(MoviesEntry.TABLE_NAME,hehe,ehh,null,null,null,null,"1");
                if (finCursor.moveToFirst()) {
                    String myId = String.valueOf(finCursor.getLong(finCursor.getColumnIndex(MoviesEntry._ID)));
                    String[] calu = new String[]{myId};
                    int count = mDb.update(MoviesEntry.TABLE_NAME, contentValues, MoviesEntry._ID + " = ?", calu);
                }
                else {
                    Log.i(LOG_TAG,"cursor msff");
                }}
            else{
                ContentValues contentValues = new ContentValues();
                contentValues.put(MoviesEntry.PLOTSUMMARY,"We dont have a plot as of yet..");
                String[] hehe = new String[]{MoviesEntry._ID};
                String ehh = MoviesEntry.PLOTSUMMARY + " LIKE " +"\'"+"%Synopsis%"+"\'";
                Cursor finCursor= mDb.query(MoviesEntry.TABLE_NAME,hehe,ehh,null,null,null,null,String.valueOf(1));
                if (finCursor.moveToFirst()) {
                    String myId = String.valueOf(finCursor.getLong(finCursor.getColumnIndex(MoviesEntry._ID)));
                    Log.i(LOG_TAG,myId);
                    String[] calu = new String[]{myId};
                    int count = mDb.update(MoviesEntry.TABLE_NAME, contentValues, MoviesEntry._ID + " = ?", calu);
                }
                else {
                    Log.i(LOG_TAG,"cursor msf");
                }
            }
*/

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
            suggest = movys.movysapp.utils.QueryUtils.dynamicSuggests(newText,category);

            runOnUiThread(new Runnable() {
                public void run() {
                    aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, suggest);
                    listView.setAdapter(aAdapter);
                    if (aAdapter.isEmpty()){
                        listView.setVisibility(View.GONE);
                    }
                    else{
                        listView.setVisibility(View.VISIBLE);
                    }aAdapter.notifyDataSetChanged();
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
        if (searchActivity!=null){

            radioGroup.setVisibility(View.VISIBLE);
            scrollView.setVisibility(GONE);
            listView.setVisibility(GONE);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
        }



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
                listView.setVisibility(GONE);
                radioGroup.setVisibility(GONE);
                progressBar.setVisibility(View.VISIBLE);
                new ExtractActivity().execute(plotTitle);
                searchView.clearFocus();
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    interstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
                                AdRequest adRequest = new AdRequest.Builder().build();
                                interstitialAd.loadAd(adRequest);
                            }
                            finish();
                        }
                    });
                }
            }
        });
        return true;
    }

    public Cursor getDetails(Long idDb) {
        String from[] = new String[]{MoviesEntry.PLOTSUMMARY,MoviesEntry.TITLE};
        String where = MoviesEntry._ID+"="+idDb;
        Cursor cursor =  mDb.query(MoviesEntry.TABLE_NAME,from,where,null,null,null,null);

        return cursor;
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (!interstitialAd.isLoading() && !interstitialAd.isLoaded()) {
                        AdRequest adRequest = new AdRequest.Builder().build();
                        interstitialAd.loadAd(adRequest);
                    }
                    finish();
                }
            });
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public void onPause() {
        if (mAdView!= null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView!= null) {
            mAdView.resume();
        }
    }
    @Override
    public void onDestroy() {
        if (mAdView!= null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
