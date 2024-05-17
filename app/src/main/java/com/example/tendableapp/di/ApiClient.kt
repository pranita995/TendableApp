package com.example.tendableapp.di

import com.example.tendableapp.retrofit.LoginAPI
import com.example.tendableapp.utils.Constants.BASE_URL
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

object ApiClient {

    var mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    val connectionTimeoutSeconds = 7L
    var mOkHttpClient = OkHttpClient
        .Builder()
        .retryOnConnectionFailure(true)
        .readTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
        .connectTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
        .writeTimeout(connectionTimeoutSeconds, TimeUnit.SECONDS)
        .addInterceptor(mHttpLoggingInterceptor)
        .build()

    var mRetrofit: Retrofit? = null


    val client: Retrofit?
        get() {
            if(mRetrofit == null){
                mRetrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
}