package com.example.smartcontrol

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("posts/{uuid}")
    suspend fun getPost(@Path("uuid") id: Int): Control

    @GET("sensors/{uuid}")
    suspend fun getSensors(@Path("uuid") id: Int): Sensors
}

data class Control(val uuid: Int)

data class Sensors(val uuid: Int)

object RetrofitClient {
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
}