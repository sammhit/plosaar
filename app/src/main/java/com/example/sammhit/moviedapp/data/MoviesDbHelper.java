package com.example.sammhit.moviedapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.sammhit.moviedapp.data.MovieContract.MoviesEntry;

/**
 * Created by sammhit on 9/4/18.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION =2;
    private final Context context;


    public MoviesDbHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        InputStream inputStream=null;
        String line = "";
        try {
            inputStream = this.context.getAssets().open("movies2017.tsv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String SQL_CREATE_TABLE = "CREATE TABLE "+ MovieContract.MoviesEntry.TABLE_NAME + " ("
                + MovieContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieContract.MoviesEntry.TITLE+" VARCHAR, "
                + MovieContract.MoviesEntry.LINK+" VARCHAR, "
                + MoviesEntry.IMDB_RATING+" VARCHAR, "
                + MovieContract.MoviesEntry.CATEGORIES+" VARCHAR, "
                + MoviesEntry.PLOT+" VARCHAR"
                + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        sqLiteDatabase.beginTransaction();
        try {
            while((line=br.readLine())!=null){
                String[] columns = line.split("\\t");
                if(columns.length!=5){
                    Log.i("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues();
                cv.put(MoviesEntry.CATEGORIES,columns[0]);
                cv.put(MoviesEntry.LINK,columns[1]);
                cv.put(MoviesEntry.IMDB_RATING,columns[2]);
                cv.put(MoviesEntry.PLOT,columns[3]);
                cv.put(MoviesEntry.TITLE,columns[4]);
                sqLiteDatabase.insert(MoviesEntry.TABLE_NAME,null,cv);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        Log.i("SQL","Transaction Succesful");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }


}
