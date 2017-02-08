package se.oscarb.trendytrailers.data;

import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";

    }
}
