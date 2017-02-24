package se.oscarb.trendytrailers.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static se.oscarb.trendytrailers.data.FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TMDB_ID;
import static se.oscarb.trendytrailers.data.FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME;

public class FavoriteMoviesContentProvider extends ContentProvider {


    // Directory of favorite movies and a single favorite movie
    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIE_WITH_TMDB_ID = 101;
    // Uri Matcher
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private FavoriteMoviesDbHelper dbHelper;

    // Associate URI's with corresponding int match
    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Endpoints
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_WITH_TMDB_ID);

        return uriMatcher;
    }

    /**
     * Initialize database connection using the dbHelper
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new FavoriteMoviesDbHelper(context);

        return true;
    }

    /**
     * Get a favorite movie by Id...
     *
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get db with dbHelper
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        // What URI?
        int match = uriMatcher.match(uri);
        Cursor returnCursor;

        // Query for favorite movies

        switch (match) {
            case FAVORITE_MOVIES:
                returnCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify content resolver
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Insert favorites to database
     *
     * @param uri
     * @param values
     * @return
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get database
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Which endpoint?
        int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITE_MOVIES:
                // Insert new movie into table of favorites
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert movie into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get db with dbHelper
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        // Number of deleted favorite movies
        int favoriteMoviesDeleted;

        // Remove movies from table of favorites
        switch (match) {
            case FAVORITE_MOVIE_WITH_TMDB_ID:
                // Get Movie ID from URI path
                String id = uri.getPathSegments().get(1);
                // Filter by this id
                favoriteMoviesDeleted = db.delete(TABLE_NAME, COLUMN_TMDB_ID + "=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify number of favorites removed
        if (favoriteMoviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return how many favorites were removed
        return favoriteMoviesDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
