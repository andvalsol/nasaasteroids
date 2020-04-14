package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.Result
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.models.getDatabase
import com.udacity.asteroidradar.models.toModelObject
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(Application()) {

    private val pictureOfDayMutableLiveData = MutableLiveData<Result<PictureOfDay>>()
    val pictureOfDayLiveData: LiveData<Result<PictureOfDay>>
        get() = pictureOfDayMutableLiveData

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val asteroidRoomDatabase = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(asteroidRoomDatabase)

    val asteroidsLiveData = asteroidRepository.getAsteroidsFromStartingDate(Date().time)

    init {
        // Get the picture of the day as soon as the ViewModel is created
        getPictureOfDay()

        coroutineScope.launch {
            // this will make asteroids live data to change when data is retrieved
            asteroidRepository.refreshAsteroids()
        }
    }

    /**
     * Function that gets the picture of the day from the [NasaAPI]
     * */
    private fun getPictureOfDay() {
        pictureOfDayMutableLiveData.value = Result.Loading

        coroutineScope.launch {
            // Surround the code with try catch, since the deferred object can throw an error
            try {
                val pictureOfDay =
                    NasaAPI.withMoshiConverter.getPictureOfDayAsync(Constants.API_KEY).await()
                pictureOfDayMutableLiveData.value = Result.Success(pictureOfDay.toModelObject())
            } catch (t: Throwable) {
                pictureOfDayMutableLiveData.value =
                    Result.Error(Exception("The error while fetching the picture of the day is ${t.localizedMessage}"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        // Cancel all work from the viewModel
        viewModelJob.cancel()
    }
}