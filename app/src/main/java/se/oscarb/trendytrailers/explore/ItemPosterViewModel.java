package se.oscarb.trendytrailers.explore;

import android.content.Context;
import android.databinding.BaseObservable;

import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.ViewModel;

public class ItemPosterViewModel extends BaseObservable implements ViewModel {

    private Context context;
    private Movie movie;

    public ItemPosterViewModel(Context context, Movie movie) {
        this.context = context;
        this.movie = movie;
    }

    @Override
    public void destroy() {

    }


    // Recycle viewModel within adapter
    public void setMovie(Movie photo) {
        this.movie = movie;
        notifyChange();
    }
}
