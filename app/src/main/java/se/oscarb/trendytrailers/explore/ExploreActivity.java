package se.oscarb.trendytrailers.explore;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.data.remote.TheMovieDbService;
import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.databinding.ActivityMainBinding;
import se.oscarb.trendytrailers.model.MovieListing;

public class ExploreActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Setup RecyclerView
        setupRecyclerView(binding.moviePosters);

        loadMoviesFromApi();

    }

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
        loadMoviesFromApi(TheMovieDbService.SORT_POPULARITY);
    }

    /**
     * Get and show movie posters from the API sorted by sortOrder
     */
    private void loadMoviesFromApi(final String sortOrder) {
        // Test retrofit
        binding.progressBar.setVisibility(View.VISIBLE);

        TheMovieDbService service = TheMovieDbServiceGenerator.getService();

        Call<MovieListing> call = service.discoverMovies(sortOrder);

        call.enqueue(new Callback<MovieListing>() {
            @Override
            public void onResponse(Call<MovieListing> call, Response<MovieListing> response) {
                // Hide progress
                binding.progressBar.setVisibility(View.GONE);

                MovieListing movieListing = response.body();

                MoviePostersAdapter moviePostersAdapter = (MoviePostersAdapter) binding.moviePosters.getAdapter();
                moviePostersAdapter.setMovieList(movieListing.getMovies());
                moviePostersAdapter.notifyDataSetChanged();

                setCheckedSortOrder(sortOrder);


            }

            @Override
            public void onFailure(Call<MovieListing> call, Throwable t) {
                Log.d("tag", "failure");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explore, menu);
        return true;
    }

    private void setCheckedSortOrder(String sortOrder) {
        int menuItemId = 0;

        switch (sortOrder) {
            case TheMovieDbService.SORT_HIGHEST_RATED:
                menuItemId = R.id.action_sort_vote_average_desc;
                break;
            case TheMovieDbService.SORT_POPULARITY:
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


        switch (id) {
            case R.id.action_sort_popularity_desc:
                sort = TheMovieDbService.SORT_POPULARITY;
            case R.id.action_sort_vote_average_desc:
                if (sort == null) sort = TheMovieDbService.SORT_HIGHEST_RATED;
                // Actions for both sort options
                if (item.isChecked()) return true;
                loadMoviesFromApi(sort);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
