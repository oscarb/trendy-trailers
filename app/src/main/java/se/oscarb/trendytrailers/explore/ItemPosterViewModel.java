package se.oscarb.trendytrailers.explore;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.ViewModel;

public class ItemPosterViewModel extends BaseObservable implements ViewModel {

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
                //.centerCrop()
                .into(imageView);
    }

    public String getImageUrl() {
        return TheMovieDbServiceGenerator.IMAGE_BASE_URL + "w185" + movie.getPosterPath();
    }

    public String getTitle() {
        return movie.getTitle();
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
