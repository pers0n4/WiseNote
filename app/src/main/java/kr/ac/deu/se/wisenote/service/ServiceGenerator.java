package kr.ac.deu.se.wisenote.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
  private final static String BASE_URL = "http://13.125.179.157";
  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
  private static Gson gson = new GsonBuilder()
    .setDateFormat("yyyy-MM-dd")
    .create();
  private static Retrofit.Builder builder = new Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(gson));
  private static Retrofit retrofit = builder.build();

  private ServiceGenerator() {}

  public static <S> S createService(Class<S> serviceClass) {
    return createService(serviceClass, null);
  }

  public static <S> S createService(Class<S> serviceClass, String authToken) {
    if (!(authToken == null)) {
      AuthenticationInterceptor interceptor =
        new AuthenticationInterceptor("Bearer " + authToken);

      if (!httpClient.interceptors().contains(interceptor)) {
        httpClient.addInterceptor(interceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();
      }
      return retrofit.create(serviceClass);
    }
    return retrofit.create(serviceClass);
  }
}
