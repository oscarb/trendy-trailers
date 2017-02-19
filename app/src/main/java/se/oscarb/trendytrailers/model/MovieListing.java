package se.oscarb.trendytrailers.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * Meta-data about a search, such as number of results, the results and pages
 */
@Parcel
public class MovieListing {
    private int page;
    private List<Movie> results;
    private int total_results;
    private int total_pages;

    public List<Movie> getMovies() {
        return results;
    }
}
