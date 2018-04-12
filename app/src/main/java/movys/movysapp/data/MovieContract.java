package movys.movysapp.data;

import android.provider.BaseColumns;

/**
 * Created by sammhit on 9/4/18.
 */

public class MovieContract {
    public static final class MoviesEntry implements BaseColumns{
        public static final String TABLE_NAME = "moviesDb";

        public static final String TITLE= "title";
        public static final String LINK= "link";

        public static final String IMDB_RATING = "rating";

        public static final String CATEGORIES = "genre";
        public static final String PLOT = "plot";
        public static final String PLOTSUMMARY ="plotSummary";


        }

}
