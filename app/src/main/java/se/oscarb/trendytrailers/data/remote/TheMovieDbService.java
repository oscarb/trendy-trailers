package se.oscarb.trendytrailers.data.remote;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import se.oscarb.trendytrailers.model.Movie;
import se.oscarb.trendytrailers.model.MovieListing;

import static se.oscarb.trendytrailers.data.remote.TheMovieDbService.SortBy.HIGHEST_RATED;
import static se.oscarb.trendytrailers.data.remote.TheMovieDbService.SortBy.POPULARITY;

/**
 * Interface for getting popular movies from The Movie Database API
 */
public interface TheMovieDbService {

    @GET("discover/movie")
    Call<MovieListing> discoverMovies(@Query("sort_by") @SortBy String sortBy);

    @GET("movie/popular")
    Call<MovieListing> getPopularMovies(@Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieListing> getTopRatedMovies(@Query("page") int page);

    @GET("movie/{movie_id}")
    Call<Movie> getMovie(@Path("movie_id") int movieId);

    @GET("movie/{movie_id}?append_to_response=videos,reviews")
    Call<Movie> getMovieWithVideosAndReviews(@Path("movie_id") int movieId);


    /* Magic Constants */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            POPULARITY,
            HIGHEST_RATED
    })
    @interface SortBy {
        String POPULARITY = "popularity.desc";
        String HIGHEST_RATED = "vote_average.desc";
    }

}
