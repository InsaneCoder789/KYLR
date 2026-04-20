package com.example.kylr.data.api

import com.example.kylr.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val authInterceptor = Interceptor { chain ->
        val key = BuildConfig.RAIL_API_KEY
        val builder = chain.request().newBuilder()
        if (key.isNotBlank()) {
            builder.header("X-RAIL-API-KEY", key)
        }
        chain.proceed(builder.build())
    }

    private val client: OkHttpClient = run {
        val b = OkHttpClient.Builder().addInterceptor(authInterceptor)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            b.addInterceptor(logging)
        }
        b.build()
    }

    val instance: UpiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.RAIL_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(UpiApi::class.java)
    }
}
