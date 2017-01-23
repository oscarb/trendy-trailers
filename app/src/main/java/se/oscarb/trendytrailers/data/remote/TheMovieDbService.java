package se.oscarb.trendytrailers.data.remote;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import se.oscarb.trendytrailers.model.MovieListing;

/**
 * Interface for getting popular movies from The Movie Database API
 */
public interface TheMovieDbService {


    /* Magic Constants */
    String SORT_POPULARITY = "popularity.desc";
    String SORT_HIGHEST_RATED = "vote_average.desc";

    @GET("discover/movie")
    Call<MovieListing> discoverMovies(@Query("sort_by") @SortBy String sortBy);

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SORT_POPULARITY,
            SORT_HIGHEST_RATED
    })
    @interface SortBy {
    }

}
