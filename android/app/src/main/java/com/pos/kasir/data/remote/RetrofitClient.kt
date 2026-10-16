package com.pos.kasir.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Ganti BASE_URL sesuai IP backend Anda (emulator: 10.0.2.2)
    const val BASE_URL = "http://10.0.2.2:3000/"

    fun create(tokenProvider: () -> String?): ApiService {
        val client = OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val req = chain.request().newBuilder()
                tokenProvider()?.let { req.addHeader("Authorization", "Bearer $it") }
                chain.proceed(req.build())
            }
            addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        }.build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
