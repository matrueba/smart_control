package com.example.smartcontrol

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("control_auto/{uuid}")
    fun setControl(@Path("uuid") uuid: String, @Body controlPost: ControlPost): Call<ControlPost>

    @POST("control_pump/{uuid}")
    fun startPump(@Path("uuid") uuid: String, @Body pumpMode: PumpMode): Call<PumpMode>

    @GET("sensor_values/{uuid}")
    suspend fun getSensors(@Path("uuid") uuid: String): Sensors

    @GET("global_status/{uuid}")
    suspend fun getGlobalStatus(@Path("uuid") uuid: String): GlobalStatus

    @GET("control_mode/{uuid}")
    suspend fun getControlMode(@Path("uuid") uuid: String): ControlMode
}

data class ControlPost(
    val mode: String,
    val activationHour: Int? = null,
    val activationMinute: Int? = null,
    val periodic: Boolean? = null,
    val repeatDays: Int? = null
)

data class PumpMode(
    val start: Boolean,
)

data class Sensors(val groundHumidity: String, val airHumidity: String,
                   val temperature: String, val waterLevel: String)

data class GlobalStatus(val auto: Boolean, val pumpStarted: Boolean,
                        val groundHumidity: String, val airHumidity: String,
                        val temperature: String, val waterLevel: String,
                        val irrigationTime: Long, val autoMinuteScheduled: Int,
                        val autoHourScheduled: Int)

data class ControlMode (val auto: Boolean)

object RetrofitClient {
    private val BASE_URL = "http://192.168.1.33:5000/api/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}