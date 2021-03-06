package se.oscarb.trendytrailers.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static se.oscarb.trendytrailers.data.FavoriteMoviesContract.FavoriteMoviesEntry;


public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite-movies.db";
    private static final int DATABASE_VERSION = 1;

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITE_MOVIES_TABLE = "CREATE TABLE " +
                FavoriteMoviesEntry.TABLE_NAME + "(" +
                FavoriteMoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoriteMoviesEntry.COLUMN_TMDB_ID + " INTEGER NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_ADULT + " BOOLEAN NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                FavoriteMoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                FavoriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                " UNIQUE (" + FavoriteMoviesEntry.COLUMN_TMDB_ID + ") ON CONFLICT REPLACE " +
                ");";


        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Replace old database with a fresh new one
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
