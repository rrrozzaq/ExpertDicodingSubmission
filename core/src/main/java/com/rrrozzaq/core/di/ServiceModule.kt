package com.rrrozzaq.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.rrrozzaq.core.BuildConfig
import com.rrrozzaq.core.data.source.remote.ApiService
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideApiService(): ApiService {
        val authInterceptor = Interceptor{ chain ->
            val req = chain.request()
            val requestHeader = req.newBuilder()
                .addHeader("Authorization", "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")
                .build()
            chain.proceed(requestHeader)
        }

        val loggingInterceptor = if(de.hdodenhof.circleimageview.BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

}