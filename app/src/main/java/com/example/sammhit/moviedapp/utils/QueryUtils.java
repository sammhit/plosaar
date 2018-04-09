package com.example.sammhit.moviedapp.utils;

import android.app.ListActivity;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sammhit on 8/4/18.
 */

public class QueryUtils {
    private static final String LOG_TAG= QueryUtils.class.getSimpleName();
    private QueryUtils() throws JSONException{}
    public final static String BASE_URL =buildUrl();

    public static ArrayList<String> extractPlot(String requestQuery) {
        Uri plotUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter("prop","extracts")
                .appendQueryParameter("explaintext","")
                .appendQueryParameter("titles",requestQuery)
                .build();

        ArrayList<String> plot = new ArrayList<String>();
        String extractString=null;
        String SAMPLE_JSON_RESPONSE = null;
        URL url = createUrl(plotUri.toString());
        SAMPLE_JSON_RESPONSE = makeHTTPRequest(url);
        Log.i(LOG_TAG,requestQuery);
        Log.i(LOG_TAG,"Hi baby");
        Log.i(LOG_TAG,SAMPLE_JSON_RESPONSE);

        try {
            JSONObject JSONroot = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONObject query = JSONroot.optJSONObject("query");
            JSONObject pages = query.optJSONObject("pages");
            Iterator keys =pages.keys();
            String defaultString = "We don't have a plot..";

            while (keys.hasNext()) {
                String currentDynamicKey = (String) keys.next();
                JSONObject currentDynamicValue = pages.optJSONObject(currentDynamicKey);
                String title = currentDynamicValue.optString("title");
                extractString = currentDynamicValue.optString("extract");
                if (extractString.equals("")) {
                    plot.add(defaultString);
                    return plot;
                }
                if (!extractString.contains("== Plot ==")){
                    plot.add(defaultString);
                    return plot;
                }
                plot.add(title);
                plot.add(extractString.split("== Plot ==")[1].split("==")[0]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return plot;
    }

    public static List<String> dynamicSuggests(String newText,String category){
        List<String> suggests=new ArrayList<String>();
        Log.i(LOG_TAG,newText);
        String bollywoodCategory = "Hindi-language_films|Indian_films";
        String hollywoodCategory = "English-language_films|American_films";
        String s= "https://en.wikipedia.org/w/api.php?action=query&format=json&list=search&srsearch=intitle:\""
                +newText
                +"\"+incategory:";
        if (category.equals("Bollywood")) {
            s = s+bollywoodCategory;
        }else{
            s= s+hollywoodCategory;
        }

        URL mUrl = createUrl(s);
        Log.i(LOG_TAG,mUrl.toString());
        String JsonResponseTitles=makeHTTPRequest(mUrl);
        Log.i(LOG_TAG,JsonResponseTitles);
        String title=null;
        try{
            JSONObject JSONroot = new JSONObject(JsonResponseTitles);
            JSONObject query = JSONroot.optJSONObject("query");
            JSONObject searchInfo = query.optJSONObject("searchinfo");
            int totalhits = searchInfo.optInt("totalhits");
            int iterItems=0;
            if (totalhits<10){
                iterItems= totalhits;
            }
            else{
                iterItems=10;
            }
            JSONArray searchJson = query.optJSONArray("search");
            for (int i=0;i<iterItems;i++){
                JSONObject eachSearchResult = searchJson.optJSONObject(i);
                title=eachSearchResult.optString("title");
                Log.i(LOG_TAG,title);
                suggests.add(title);
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return suggests;
    }

    private static String makeHTTPRequest(URL mUrl) {
        String jsonResponse= "";
        if(mUrl==null){
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try{
            httpURLConnection =(HttpURLConnection) mUrl.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent","MoviedApp/1.0");
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode()==200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);
            }
            else{
                Log.e(LOG_TAG,"Error in response code:"+httpURLConnection.getResponseCode());

            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Error getting message from JSON",e);
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line!=null){
                output.append(line);
                line=bufferedReader.readLine();
            }

        }
        return  output.toString();

    }

    private static URL createUrl(String stringUrl) {
        URL url=null;
        try {
            url=new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String buildUrl(){
        Uri.Builder builder= new Uri.Builder();
        builder.scheme("https")
                .authority("en.wikipedia.org")
                .appendPath("w")
                .appendPath("api.php")
                .appendQueryParameter("action","query")
                .appendQueryParameter("format","json");
        return builder.build().toString();
    }

    public static void getMovies(String movieId){
        String baseUrl = "http://www.omdbapi.com/?apikey=f9cbe152&i=";

        URL url = createUrl(baseUrl+movieId);
        String JSONResponse =null;
        JSONResponse =makeHTTPRequest(url);
        JSONObject JsonRoot = null;
        try {
            JsonRoot = new JSONObject(JSONResponse);
            String imageLink= JsonRoot.optString("Poster");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
