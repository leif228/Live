package com.wj.work.http;

import com.wj.work.BuildConfig;
import com.wj.work.constant.AppConstant;
import com.wj.work.http.support.HttpLogger;
import com.wj.work.http.support.SafeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitManger
 *
 * @author tanghu
 * @title RetrofitManger
 * @package com.duoqio.gonet.http
 * @date 2019/12/3 9:43
 */
public class RetrofitManger {

    private static volatile RetrofitManger mInstance;
    private Retrofit.Builder mRetrofitBuilder;

    public static RetrofitManger $() {
        if (mInstance == null) {
            synchronized (RetrofitManger.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManger();
                }
            }
        }
        return mInstance;
    }

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new SafeTypeAdapterFactory())
            .serializeNulls()
            .create();

    private RetrofitManger() {
        //开启Log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        if (BuildConfig.DEBUG) {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

//        //ok http client
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(logInterceptor)
                .hostnameVerifier((hostname, session) -> true)
                .retryOnConnectionFailure(true)
                .writeTimeout(AppConstant.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(AppConstant.HTTP_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(AppConstant.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // Retrofit Builder
        mRetrofitBuilder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    public <T> T createApi(Class<T> clazz) {
        return mRetrofitBuilder
                .baseUrl(AppConstant.RETROFIT_BASE_URL)
                .build()
                .create(clazz);
    }

    public <T> T createApi(String baseUrl, Class<T> clazz) {
        return mRetrofitBuilder.baseUrl(baseUrl).build().create(clazz);
    }
}
