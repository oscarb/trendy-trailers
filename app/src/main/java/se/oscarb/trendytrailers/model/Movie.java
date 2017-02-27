package se.oscarb.trendytrailers.model;

import org.parceler.Parcel;

import java.util.List;

/**
 * A Movie with properties as defined by The Movie Database
 */
@Parcel
public class Movie {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private int id;
    private String original_title;
    private String title;
    private String backdrop_path;
    private float vote_average;
    private ReviewListing reviews;
    private VideoListing videos;

    // Getters

    public boolean isAdultMovie() {
        return adult;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdrop_path = backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String posterPath) {
        this.poster_path = posterPath;
    }

    public List<Review> getReviews() {
        return reviews.getReviews();
    }

    public List<Video> getTrailers() {
        return videos.getResults();
    }
    // Setters

    public String getReleaseDate() {
        return release_date;
    }

    public void setReleaseDate(String releaseDate) {
        this.release_date = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(float voteAverage) {
        this.vote_average = voteAverage;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String originalTitle) {
        this.original_title = originalTitle;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setAdult(int adult) {
        this.adult = adult > 0;
    }

    // Other methods

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Movie)) return false;
        Movie otherMovie = (Movie) other;
        return this.getId() == otherMovie.getId();
    }
}
