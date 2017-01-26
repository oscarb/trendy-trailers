package se.oscarb.trendytrailers.model;

/**
 * A Movie with properties as defined by The Movie Database
 */
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


    public boolean isAdultMovie() {
        return adult;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public String getOriginalTitle() {
        return original_title;
    }
}
