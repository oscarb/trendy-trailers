package se.oscarb.trendytrailers.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import se.oscarb.trendytrailers.model.MovieListing;

/**
 * Interface for getting popular movies from The Movie Database API
 */
public interface TheMovieDbService {

    @GET("discover/movie")
    Call<MovieListing> discoverMovies(@Query("sort_by") String sortBy);
}
