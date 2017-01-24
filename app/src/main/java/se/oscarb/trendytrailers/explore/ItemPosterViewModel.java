package se.oscarb.trendytrailers.explore;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import se.oscarb.trendytrailers.data.remote.TheMovieDbServiceGenerator;
import se.oscarb.trendytrailers.detail.DetailActivity;
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
                .into(imageView);
    }

    public String getImageUrl() {
        return TheMovieDbServiceGenerator.IMAGE_BASE_URL + "w185" + movie.getPosterPath();
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public void onPosterClick(View view) {
        Toast.makeText(view.getContext(), "" + movie.getId(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(view.getContext(), DetailActivity.class);
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
