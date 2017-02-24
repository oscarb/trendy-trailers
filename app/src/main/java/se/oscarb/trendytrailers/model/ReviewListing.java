package se.oscarb.trendytrailers.model;

import org.parceler.Parcel;

import java.util.Collections;
import java.util.List;

/**
 * Search result of Movie reviews
 */
@Parcel
public class ReviewListing {
    private int page;
    private List<Review> results;
    private int total_pages;
    private int total_results;

    public List<Review> getReviews() {
        if (results == null) {
            return Collections.emptyList();
        }
        return results;
    }
}
