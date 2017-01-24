package se.oscarb.trendytrailers.detail;

import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.ViewModel;


public class DetailViewModel implements ViewModel {

    private Movie movie;

    public DetailViewModel(Movie movie) {
        this.movie = movie;
    }


    public String getTitle() {
        return movie.getOriginalTitle();
    }

    @Override
    public void destroy() {
        // Empty for now
    }
}
