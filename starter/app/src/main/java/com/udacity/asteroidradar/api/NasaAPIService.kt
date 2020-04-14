package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.models.AsteroidDatabase
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.models.PictureOfDayNetwork
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Create the Moshi converter
private val moshi =
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

// Create the retrofit instance
private val retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // In change of converting from JSON to Kotlin objects
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://api.nasa.gov/")
        .build()

interface NasaAPIService {

    @GET("planetary/apod?api_key=3fqu0VKDiKPr28whgibao34bU7frI0TzgAPTl1VT")
    fun getPictureOfDayAsync():
            Deferred<PictureOfDayNetwork>

    @GET("https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=3fqu0VKDiKPr28whgibao34bU7frI0TzgAPTl1VT")
    fun getAsteroid():
            Deferred<List<AsteroidDatabase>>
}

// Create a single instance of the NasaAPI
object NasaAPI {
    val nasaAPIService by lazy { retrofit.create(NasaAPIService::class.java) }
}