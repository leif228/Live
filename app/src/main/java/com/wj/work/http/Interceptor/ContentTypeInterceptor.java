package com.wj.work.http.Interceptor;

import androidx.annotation.NonNull;

import com.wj.work.http.Interceptor.BaseInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tanghu on 2018/6/14.
 */
public class ContentTypeInterceptor implements BaseInterceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request build = chain.request().newBuilder()
                .addHeader(HEADER_CONTENT_TYPE_NAME, HEADER_CONTENT_TYPE_VALUE)
                .build();
        return chain.proceed(build);
    }
}
