package se.oscarb.trendytrailers.model;

public class Movie {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private int id;
    private String title;
    private String backdrop_path;
    private int vote_average;


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

    public int getVoteAverage() {
        return vote_average;
    }
}