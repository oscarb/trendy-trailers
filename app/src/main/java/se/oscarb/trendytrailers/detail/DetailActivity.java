package se.oscarb.trendytrailers.detail;

import android.content.ContentValues;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.data.FavoriteMoviesContract;
import se.oscarb.trendytrailers.data.remote.TheMovieDbService;
import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.databinding.ActivityDetailBinding;
import se.oscarb.trendytrailers.explore.ItemPosterViewModel;
import se.oscarb.trendytrailers.model.Movie;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private ActivityDetailBinding binding;
    private Movie movie;


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

    private boolean addToFavorites(Movie movie) {
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
        return uri != null;
    }

    private boolean removeFromFavorites(Movie movie) {
        // Uri for removing movie from favorites
        Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movie.getId())).build();

        return getContentResolver().delete(uri, null, null) > 0;
    }

    /** Bind movie with the View-Model for showing it in the UI */
    private void bindMovie(Movie movie) {

        DetailViewModel detailViewModel = new DetailViewModel(movie);
        binding.setDetailViewModel(detailViewModel);

        updateFavoriteIcon(isFavorite(movie));
    }

    private boolean isFavorite(Movie movie) {
        // Check with ContentProvider if movie is a favorite movie?
        Uri uri = FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI;
        String selection = FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TMDB_ID + " = ?";
        String[] selectionArgs = {Integer.toString(movie.getId())};
        Cursor cursor = getContentResolver().query(uri, null, selection, selectionArgs, null);

        return cursor != null && cursor.getCount() == 1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_toggle_favorite:
                Movie movie = binding.getDetailViewModel().getMovie();
                String message;

                if (isFavorite(movie)) {
                    message = "Could not remove " + movie.getTitle() + " from favorites";
                    if (removeFromFavorites(movie)) {
                        message = "Removed " + movie.getTitle() + " from favorites";
                    }
                } else {
                    addToFavorites(movie);
                    message = "Added " + movie.getTitle() + " to favorites";
                }
                updateFavoriteIcon(isFavorite(movie));
                Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        MenuItem item = binding.toolbar.getMenu().findItem(R.id.action_toggle_favorite);

        int icon = (isFavorite) ? R.drawable.ic_favorite_24dp : R.drawable.ic_favorite_border_24dp;
        int title = (isFavorite) ? R.string.remove_from_favorites : R.string.add_to_favorites;

        item.setIcon(icon);
        item.setTitle(title);
    }
}
