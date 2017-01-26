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

    ActivityDetailBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(binding.toolbar);

        int movieId = getIntent().getIntExtra(ItemPosterViewModel.EXTRA_MOVIE_TMDB_ID, -1);

        if (movieId == -1) return;

        setTitle("Loading movie...");
        searchApiForMovie(movieId);

        /*
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

    private void searchApiForMovie(int movieId) {
        TheMovieDbService service = TheMovieDbServiceGenerator.getService();

        Call<Movie> call = service.getMovie(movieId);

        call.enqueue(new Callback<Movie>() {
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

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_LONG).show();
            }
        });


    }

    private void bindMovie(Movie movie) {

        DetailViewModel detailViewModel = new DetailViewModel(movie);
        binding.setDetailViewModel(detailViewModel);
    }

}
