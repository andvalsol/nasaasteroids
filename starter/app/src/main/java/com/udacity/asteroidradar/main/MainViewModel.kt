package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel : ViewModel() {

    val pictureOfDayMutableLiveData = MutableLiveData<Result<PictureOfDay>>()
    private val pictureOfDayLiveData: LiveData<Result<PictureOfDay>>
        get() = pictureOfDayMutableLiveData

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        // Get the picture of the day as soon as the ViewModel is created
        getPictureOfDay()
    }

    /**
     * Function that gets the picture of the day from the [NasaAPI]
     * */
    private fun getPictureOfDay() {
        pictureOfDayMutableLiveData.value = Result.Loading

        coroutineScope.launch {
            // Surround the code with try catch, since the deferred object can throw an error
            try {
                val pictureOfDay = NasaAPI.nasaAPIService.getPictureOfDayAsync().await()
//                pictureOfDayMutableLiveData.value = Result.Success(pictureOfDay)
            } catch (t: Throwable) {
                pictureOfDayMutableLiveData.value = Result.Error(Exception("The error while fetching the picture of the day is ${t.localizedMessage}"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        // Cancel all work from the viewModel
        viewModelJob.cancel()
    }
}