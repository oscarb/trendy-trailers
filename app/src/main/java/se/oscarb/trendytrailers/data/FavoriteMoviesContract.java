package se.oscarb.trendytrailers.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteMoviesContract {

    // Which Content Provider to access
    public static final String AUTHORITY = "se.oscarb.trendytrailers";

    // Build content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Possible paths for content provider
    public static final String PATH_FAVORITE_MOVIES = "favorite-movies";


    public static final class FavoriteMoviesEntry implements BaseColumns {

        // Content URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_TMDB_ID = "tmdbId";
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
