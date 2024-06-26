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

        val hostname = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/GyhWVHsOXNZc6tGTNd15kXF9YD0kEZaGxYn6MUva5jY=")
            .add(hostname, "sha256/Wec45nQiFwKvHtuHxSAMGkt19k+uPSw9JlEkxhvYPHk=")
            .add(hostname, "sha256/lmo8/KPXoMsxI+J9hY+ibNm2r0IYChmOsF9BxD74PVc=")
            .build()
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .certificatePinner(certificatePinner)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

}