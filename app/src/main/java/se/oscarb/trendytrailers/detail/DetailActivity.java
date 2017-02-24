package se.oscarb.trendytrailers.detail;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import java.util.List;

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
import se.oscarb.trendytrailers.model.Review;
import se.oscarb.trendytrailers.model.Video;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String STATE_REVIEW_LIST = "reviews";

    // YouTube properties
    private static final String NAME_YOUTUBE = "YouTube";
    private static final String SCHEME_YOUTUBE = "https";
    private static final String AUTHORITY_YOUTUBE = "www.youtube.com";
    private static final String PATH_YOUTUBE_PLAYLIST = "watch_videos";
    private static final String PATH_YOUTUBE_VIDEO = "watch";
    private static final String QUERY_PARAMETER_YOUTUBE_VIDEO = "v";
    private static final String QUERY_PARAMETER_YOUTUBE_PLAYLIST = "video_ids";

    private ActivityDetailBinding binding;
    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        setSupportActionBar(binding.toolbar);

        /** Get movie ID from ExploreActivity */
        movie = Parcels.unwrap(getIntent().getParcelableExtra(ItemPosterViewModel.EXTRA_MOVIE));
        if (movie == null) return;

        setTitle(movie.getTitle());
        bindMovie(movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupReviewsRecyclerView(binding.movieReviews);

        if (savedInstanceState != null) {
            List<Review> reviews = Parcels.unwrap(savedInstanceState.getParcelable(STATE_REVIEW_LIST));
            updateReviewsRecyclerView(reviews);
        } else {
            searchApiForMovieDetails(movie);
        }

    }

    private void setupReviewsRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        RecyclerView.Adapter adapter = new ReviewsAdapter();
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ReviewsAdapter reviewsAdapter = (ReviewsAdapter) binding.movieReviews.getAdapter();
        if (reviewsAdapter.getReviews().isEmpty()) return;
        outState.putParcelable(STATE_REVIEW_LIST, Parcels.wrap(reviewsAdapter.getReviews()));

    }


    /**
     * Initiate a search for movie with id movieId to the TMDb API
     */
    private void searchApiForMovieDetails(Movie movie) {
        TheMovieDbService service = TheMovieDbServiceGenerator.getService();
        Call<Movie> call = service.getMovieWithVideosAndReviews(movie.getId());

        call.enqueue(new Callback<Movie>() {
            /** Show detailed information for the movie when request is successful */
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (!response.isSuccessful()) {
                    Snackbar.make(binding.getRoot(), "Error", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Movie movie = response.body();
                displayReviews(movie);
                displayTrailerButton(movie);

            }

            /** Show information to the user that data failed to load */
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Snackbar.make(binding.getRoot(), R.string.error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addFloatingActionButton(final Uri uri) {
        // Add the Floating Action Button
        FloatingActionButton fab = binding.fab;
        fab.setVisibility(View.VISIBLE);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    /**
     * If there are trailers, the floating action button will be shown and clicking it will play a
     * playlist of trailers on YouTube
     *
     * @param movie
     */
    private void displayTrailerButton(Movie movie) {
        //https://www.youtube.com/watch_videos?video_ids=BaJkHrVEh9w,tIwU5haWs4k
        if (movie.getTrailers().isEmpty()) return;

        try {
            addFloatingActionButton(getYouTubeVideoUri(movie));
        } catch (NoVideosException e) {
            e.printStackTrace();
        }
    }

    /**
     * Given a set of YouTube keys, create an URL for showing the videos
     */
    private Uri getYouTubeVideoUri(Movie movie) throws NoVideosException {
        // Single video: https://www.youtube.com/watch?v=dQw4w9WgXcQ
        // Playlist of videos: https://www.youtube.com/watch_videos?video_ids=otCpCn0l4Wo,ymNFyxvIdaM&title=Dance

        if (movie.getTrailers().size() == 0) throw new NoVideosException("No videos");

        String videoIds = "";
        for (Video video : movie.getTrailers()) {
            if (video.getSite().equals(NAME_YOUTUBE)) {
                videoIds += video.getKey() + ",";
            }
        }
        // Remove last comma
        videoIds = videoIds.substring(0, videoIds.length() - 1);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME_YOUTUBE)
                .authority(AUTHORITY_YOUTUBE);
        if (movie.getTrailers().size() == 1) {
            builder.appendPath(PATH_YOUTUBE_VIDEO)
                    .appendQueryParameter(QUERY_PARAMETER_YOUTUBE_VIDEO, videoIds);
        } else if (movie.getTrailers().size() >= 1) {
            builder.appendPath(PATH_YOUTUBE_PLAYLIST)
                    .appendQueryParameter(QUERY_PARAMETER_YOUTUBE_PLAYLIST, videoIds)
                    .appendQueryParameter("title", movie.getTitle() + " trailers");
        }

        return builder.build();
    }

    private void displayReviews(Movie movie) {
        updateReviewsRecyclerView(movie.getReviews());
    }

    private void updateReviewsRecyclerView(List<Review> reviews) {
        if (reviews.isEmpty()) {
            binding.reviewHeader.setText(R.string.no_reviews);
            return;
        }

        binding.reviewHeader.setText(R.string.reviews);

        ReviewsAdapter reviewsAdapter = (ReviewsAdapter) binding.movieReviews.getAdapter();
        reviewsAdapter.setReviews(reviews);
        reviewsAdapter.notifyDataSetChanged();
        binding.movieReviews.scrollToPosition(0);
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

    /**
     * Bind movie with the View-Model for showing it in the UI
     */
    private void bindMovie(Movie movie) {

        DetailViewModel detailViewModel = new DetailViewModel(movie);
        binding.setDetailViewModel(detailViewModel);
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
        updateFavoriteIcon(isFavorite(movie));
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

    private class NoVideosException extends Exception {
        public NoVideosException(String message) {
            super(message);
        }

        public NoVideosException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
