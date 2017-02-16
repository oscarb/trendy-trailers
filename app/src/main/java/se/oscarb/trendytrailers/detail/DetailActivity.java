package se.oscarb.trendytrailers.detail;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.data.FavoriteMoviesContract;
import se.oscarb.trendytrailers.data.FavoriteMoviesDbHelper;
import se.oscarb.trendytrailers.data.remote.TheMovieDbService;
import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.databinding.ActivityDetailBinding;
import se.oscarb.trendytrailers.explore.ItemPosterViewModel;
import se.oscarb.trendytrailers.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(binding.toolbar);

        /** Get movie ID from ExploreActivity */
        int movieId = getIntent().getIntExtra(ItemPosterViewModel.EXTRA_MOVIE_TMDB_ID, -1);

        if (movieId == -1) return;

        setTitle(getString(R.string.loading_movie));
        searchApiForMovie(movieId);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup database connection
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Initiate a search for movie with id movieId to the TMDb API
     */
    private void searchApiForMovie(int movieId) {
        TheMovieDbService service = TheMovieDbServiceGenerator.getService();
        Call<Movie> call = service.getMovie(movieId);

        call.enqueue(new Callback<Movie>() {
            /** Show detailed information for the movie when request is successful */
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful()) {
                    Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Movie movie = response.body();
                setTitle(movie.getTitle());

                bindMovie(movie);
            }

            /** Show information to the user that data failed to load */
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                setTitle(R.string.app_name);
                Snackbar.make(binding.getRoot(), R.string.error, Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void addToFavorites(Movie movie) {
        ContentValues values = new ContentValues();

        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TMDB_ID, movie.getId());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ADULT, movie.isAdultMovie());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        Uri uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, values);

        // TODO: Show with snackbar or icon that movie was added to favorites

    }

    private boolean removeFromFavorites(int movieId) {

        // Uri for removing movie from favorites
        Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();

        // Remove using the content resolver
        getContentResolver().delete(uri, null, null);

        // TODO: Restart loader for requering movies in list of favorites?


        return database.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TMDB_ID + " = " + movieId, null) > 0;
    }

    /** Bind movie with the View-Model for showing it in the UI */
    private void bindMovie(Movie movie) {

        DetailViewModel detailViewModel = new DetailViewModel(movie);
        binding.setDetailViewModel(detailViewModel);
    }

}
