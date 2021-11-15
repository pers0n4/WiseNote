package kr.ac.deu.se.wisenote.service;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
  private final static String BASE_URL = "http://13.125.179.157/";
  private static OkHttpClient.Builder httpClient = null;
  private static Retrofit.Builder builder = null;
  private static Retrofit retrofit = null;

  public ServiceGenerator() {
    httpClient = new OkHttpClient.Builder();
    builder = new Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create());
    retrofit = builder.build();
  }

  public static <S> S createService(Class<S> serviceClass) {
    return createService(serviceClass, null);
  }

  public static <S> S createService(Class<S> serviceClass, final String authToken) {
    if (!TextUtils.isEmpty(authToken)) {
      AuthenticationInterceptor interceptor =
        new AuthenticationInterceptor("Bearer " + authToken);

      if (!httpClient.interceptors().contains(interceptor)) {
        httpClient.addInterceptor(interceptor);

        builder.client(httpClient.build());
        retrofit = builder.build();
      }
    }
    return retrofit.create(serviceClass);
  }
}
