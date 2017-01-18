package se.oscarb.trendytrailers.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class TheMovieDbServiceGenerator {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    private static TheMovieDbService service;

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create());

    public static TheMovieDbService getService() {
        if (service == null) {
            Retrofit retrofit = builder.build();
            service = retrofit.create(TheMovieDbService.class);
        }
        return service;
    }

}
