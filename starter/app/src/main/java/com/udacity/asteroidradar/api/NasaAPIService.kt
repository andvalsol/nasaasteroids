package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.models.PictureOfDayNetwork
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Create the Moshi converter
private val moshi =
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

// Create the retrofit instance with a Moshi converter
private val retrofitWithMoshi =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // In change of converting from JSON to Kotlin objects
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()

private val retrofitWithSingleConverter =
    Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()

interface NasaAPIService {

    @GET("planetary/apod")
    fun getPictureOfDayAsync(@Query("api_key") apiKey: String):
            Deferred<PictureOfDayNetwork>

    @GET("neo/rest/v1/feed")
    fun getAsteroidsAsync(@Query("start_date") startDate: String,
                          @Query("end_date") endDate: String,
                          @Query("api_key") apiKey: String):
            Deferred<String>

}

// Create a single instance of the NasaAPI
object NasaAPI {
    val withMoshiConverter: NasaAPIService by lazy { retrofitWithMoshi.create(NasaAPIService::class.java) }

    val withScalarsConverter: NasaAPIService by lazy { retrofitWithSingleConverter.create(NasaAPIService::class.java) }
}