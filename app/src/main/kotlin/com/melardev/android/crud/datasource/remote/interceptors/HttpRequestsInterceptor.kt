package com.melardev.android.crud.datasource.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HttpRequestsInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Adding Authorization Header
        // Request newRequest = originalRequest.newBuilder().addHeader("Authorization", "Bearer ....").build();

        /* Adding Access token as query parameter
        HttpUrl originalUrl = originalRequest.url();
        HttpUrl url = originalUrl.newBuilder()
                .addQueryParameter("access_token", ".......")
                .build();

        Request.Builder requestBuilder = originalRequest.newBuilder().url(url);
        Request newRequest = requestBuilder.build();
        */
        return chain.proceed(originalRequest)
    }
}
