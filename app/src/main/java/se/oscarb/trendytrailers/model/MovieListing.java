package se.oscarb.trendytrailers.model;

import android.graphics.Movie;

import java.util.List;

public class MovieListing {
    private int page;
    private List<Movie> movies;
    private int total_results;
    private int total_pages;

    public List<Movie> getMovies() {
        return movies;
    }
}
