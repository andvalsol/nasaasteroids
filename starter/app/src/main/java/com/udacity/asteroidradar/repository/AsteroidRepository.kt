package com.udacity.asteroidradar.repository

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

    private fun Date.formattedApiDate(): String {
        val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return formatter.format(this)
    }

    fun getAsteroidsFromStartingDate() =
        Transformations.map(roomDatabase.asteroidDao.getAsteroids(Calendar.getInstance().time.formattedApiDate())) {
            it.toListDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids =
                    NasaAPI.withScalarsConverter.getAsteroidsAsync(
                        setStringFormatFromDate(Date()),
                        setStringFormatFromDate(Date(), plusDays = 7),
                        Constants.API_KEY
                    ).await()
                roomDatabase.asteroidDao.insertAll(parseAsteroidsJsonResult(JSONObject(asteroids)))
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    // We need the following format 2020-04-14
    private fun setStringFormatFromDate(date: Date, plusDays: Int = 0): String {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.DATE, plusDays)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        return dateFormat.format(cal.time)
    }
}