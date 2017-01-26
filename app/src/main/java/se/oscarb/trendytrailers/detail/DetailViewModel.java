package se.oscarb.trendytrailers.detail;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.ViewModel;

/**
 * View-Model for detailed movie information
 */
public class DetailViewModel implements ViewModel {

    private Movie movie;

    public DetailViewModel() {
    }

    public DetailViewModel(Movie movie) {
        this.movie = movie;
    }

    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    public String getTitle() {
        return movie.getOriginalTitle();
    }

    public String getPosterUrl() {
        return TheMovieDbServiceGenerator.IMAGE_BASE_URL + "w185" + movie.getPosterPath();
    }

    public String getReleaseDate() {
        return movie.getReleaseDate();
    }

    public float getRating() {
        return movie.getVoteAverage() / 2;
    }

    public String getPlotSynopsis() {
        return movie.getOverview();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void destroy() {
        // Empty for now
    }
}
