package se.oscarb.trendytrailers.explore;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.data.FavoriteMoviesContract;
import se.oscarb.trendytrailers.data.FavoriteMoviesDbHelper;
import se.oscarb.trendytrailers.data.remote.TheMovieDbService;
import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.databinding.ActivityExploreBinding;
import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.MovieListing;

public class ExploreActivity extends AppCompatActivity {

    private ActivityExploreBinding binding;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_explore);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup RecyclerView
        setupRecyclerView(binding.moviePosters);

        loadMoviesFromApi();

        // Setup database connection
        FavoriteMoviesDbHelper dbHelper = new FavoriteMoviesDbHelper(this);
        database = dbHelper.getWritableDatabase();

    }

    /**
     * Configure RecyclerView with adapter and layout
     */
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        RecyclerView.Adapter adapter = new MoviePostersAdapter();
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Get and show movie posters from the API sorted by popularity by default
     */
    private void loadMoviesFromApi() {
        loadMoviesFromApi(TheMovieDbService.SortBy.POPULARITY);
    }

    /**
     * Get and show movie posters from the API sorted by sortOrder
     */
    private void loadMoviesFromApi(final String sortOrder) {
        binding.progressBar.setVisibility(View.VISIBLE);

        TheMovieDbService service = TheMovieDbServiceGenerator.getService();


        Call<MovieListing> call;
        switch (sortOrder) {
            case TheMovieDbService.SortBy.POPULARITY:
                call = service.getPopularMovies();
                break;
            case TheMovieDbService.SortBy.HIGHEST_RATED:
                call = service.getTopRatedMovies();
                break;
            default:
                call = service.getPopularMovies();
                break;
        }

        call.enqueue(new Callback<MovieListing>() {
            /** Populate RecyclerView with API data and fade it into view */
            @Override
            public void onResponse(Call<MovieListing> call, Response<MovieListing> response) {
                // Hide progress
                binding.progressBar.setVisibility(View.GONE);

                if (!response.isSuccessful()) {
                    Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_LONG).show();
                    return;
                }

                MovieListing movieListing = response.body();

                MoviePostersAdapter moviePostersAdapter = (MoviePostersAdapter) binding.moviePosters.getAdapter();
                moviePostersAdapter.setMovieList(movieListing.getMovies());
                moviePostersAdapter.notifyDataSetChanged();
                binding.moviePosters.scrollToPosition(0);

                setCheckedSortOrder(sortOrder);

                AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(1000);
                anim.setRepeatCount(0);
                binding.moviePosters.startAnimation(anim);

            }

            /** Display message to user that data could not load */
            @Override
            public void onFailure(Call<MovieListing> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(), R.string.error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void displayFavoriteMovies() {

        // TODO: Load from ContentResolver
        // TODO: Do it async!

        Cursor cursor = getFavoriteMoviesCursor();
        List<Movie> movieList = Collections.emptyList();

        while (cursor.moveToNext()) {
            Movie movie = new Movie();

            movie.setId(cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TMDB_ID)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_POSTER_PATH)));
            movie.setAdult(cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ADULT)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_OVERVIEW)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_RELEASE_DATE)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_TITLE)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_BACKDROP_PATH)));
            movie.setVoteAverage(cursor.getFloat(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_VOTE_AVERAGE)));

            movieList.add(movie);

        }

        cursor.close();

        // TODO: Show movielist
    }

    private Cursor getFavoriteMoviesCursor() {
        return database.query(
                FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteMoviesContract.FavoriteMoviesEntry._ID
        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explore, menu);
        return true;
    }

    /** Update menu to reflect current sortOrder */
    private void setCheckedSortOrder(String sortOrder) {
        int menuItemId = 0;

        switch (sortOrder) {
            case TheMovieDbService.SortBy.HIGHEST_RATED:
                menuItemId = R.id.action_sort_vote_average_desc;
                break;
            case TheMovieDbService.SortBy.POPULARITY:
                menuItemId = R.id.action_sort_popularity_desc;
                break;
        }
        if (menuItemId != 0) {
            binding.toolbar.getMenu().findItem(menuItemId).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        String sort = null;

        // Since both sort options execute the same code, the break can be omitted for the first case
        switch (id) {
            case R.id.action_sort_popularity_desc:
                sort = TheMovieDbService.SortBy.POPULARITY;
            case R.id.action_sort_vote_average_desc:
                if (sort == null) sort = TheMovieDbService.SortBy.HIGHEST_RATED;
                // Actions for both sort options
                if (item.isChecked()) return true;
                loadMoviesFromApi(sort);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
