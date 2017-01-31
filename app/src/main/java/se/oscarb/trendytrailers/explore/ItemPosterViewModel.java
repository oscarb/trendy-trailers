package se.oscarb.trendytrailers.explore;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import se.oscarb.trendytrailers.R;
import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.detail.DetailActivity;
import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.ViewModel;

/**
 * View-Model for each item in the RecyclerView, ie each movie poster
 */
public class ItemPosterViewModel extends BaseObservable implements ViewModel {

    public final static String EXTRA_MOVIE_TMDB_ID = "se.oscarb.trendytrailers.TMDB_ID";

    private Context context;
    private Movie movie;

    public ItemPosterViewModel(Context context, Movie movie) {
        this.context = context;
        this.movie = movie;
    }

    // Change image
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.ic_movie_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(imageView);
    }

    public String getImageUrl() {
        return TheMovieDbServiceGenerator.IMAGE_BASE_URL + "w185" + movie.getPosterPath();
    }

    public String getTitle() {
        return movie.getTitle();
    }

    /** Show detailed information about the movie when the poster image is clicked */
    public void onPosterClick(View view) {
        Intent intent = new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_TMDB_ID, movie.getId());
        view.getContext().startActivity(intent);
    }

    @Override
    public void destroy() {
        context = null;
    }


    // Recycle viewModel within adapter
    public void setMovie(Movie movie) {
        this.movie = movie;
        notifyChange();
    }
}
