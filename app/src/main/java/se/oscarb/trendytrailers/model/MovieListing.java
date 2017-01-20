package se.oscarb.trendytrailers.model;

import java.util.List;

public class MovieListing {
    private int page;
    private List<Movie> results;
    private int total_results;
    private int total_pages;

    public List<Movie> getMovies() {
        return results;
    }
}
