package se.oscarb.trendytrailers.detail;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.data.remote.TheMovieDbService;
import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.databinding.ActivityDetailBinding;
import se.oscarb.trendytrailers.explore.ItemPosterViewModel;
import se.oscarb.trendytrailers.model.Movie;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;


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

        /* Hide FAB for now, might make use of it later...
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

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

    /** Bind movie with the View-Model for showing it in the UI */
    private void bindMovie(Movie movie) {

        DetailViewModel detailViewModel = new DetailViewModel(movie);
        binding.setDetailViewModel(detailViewModel);
    }

}
