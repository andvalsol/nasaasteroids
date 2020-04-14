package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.models.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteroids(context: Context, parameters: WorkerParameters): CoroutineWorker(context, parameters) {

    companion object {
        val name = "RefreshAsteroids"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids()

            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}