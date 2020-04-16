package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        refreshAsteroidsWork()
    }

    private fun refreshAsteroidsWork() {
        applicationScope.launch {

            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setRequiresDeviceIdle(true)
                }
                .build()

            val periodicWorkRequest =
                PeriodicWorkRequestBuilder<RefreshAsteroids>(
                    1,
//                    TimeUnit.MINUTES, => Used for testing purposes
                    TimeUnit.DAYS
                )
                    .setConstraints(constraints)
                    .build()

            WorkManager.getInstance()
                .enqueueUniquePeriodicWork(
                    RefreshAsteroids.name,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicWorkRequest
                )
        }
    }
}