package se.oscarb.trendytrailers.data.remote;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import se.oscarb.trendytrailers.BuildConfig;

public class TheMovieDbServiceGenerator {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    /**
     * Add API_KEY to all requests
     */
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            HttpUrl newUrl = original.url().newBuilder()
                    .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                    .build();

            Request.Builder requestBuilder = original.newBuilder().url(newUrl);
            Request newRequest = requestBuilder.build();

            return chain.proceed(newRequest);
        }
    }).build();


    private static TheMovieDbService service;

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .client(okHttpClient)
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
