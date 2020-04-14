package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaAPI
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.models.AsteroidRoomDatabase
import com.udacity.asteroidradar.models.toListDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val roomDatabase: AsteroidRoomDatabase) {

    fun getAsteroidsFromStartingDate(startDate: Long) =
        Transformations.map(roomDatabase.asteroidDao.getAsteroids(startDate)) {
            it.toListDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids =
                NasaAPI.withScalarsConverter.getAsteroidsAsync(
                    setStringFormatFromDate(Date()),
                    setStringFormatFromDate(Date(), plusDays = 7),
                    Constants.API_KEY
                ).await()
            roomDatabase.asteroidDao.insertAll(parseAsteroidsJsonResult(JSONObject(asteroids)))
        }
    }

    // We need the following format 2020-04-14
    private fun setStringFormatFromDate(date: Date, plusDays: Int = 0): String {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, plusDays)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        Log.d("Getn", dateFormat.format(cal.time))

        return dateFormat.format(cal.time)
    }
}