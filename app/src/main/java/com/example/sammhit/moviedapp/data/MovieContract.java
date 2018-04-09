package com.example.sammhit.moviedapp.data;

import android.provider.BaseColumns;

/**
 * Created by sammhit on 9/4/18.
 */

public class MovieContract {
    public static final class MoviesEntry implements BaseColumns{
        public static final String TABLE_NAME = "moviesDb";
        public static final String MOVIE_ID= "movieId";
        public static final String TITLE= "title";
        public static final String LINK= "link";
        public static final String TYPE= "type";
        public static final String IMDB_RATING = "rating";
        public static final String YEAR = "year";
        public static final String CATEGORIES = "category";
        public static final String VOTES = "votes";
        public static final String DIRECTOR = "director";


        }

}
