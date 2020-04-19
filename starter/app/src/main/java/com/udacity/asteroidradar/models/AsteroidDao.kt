package com.udacity.asteroidradar.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    // Get all the asteroids from a given date onwards
    @Query("SELECT * FROM asteroiddatabase WHERE closeApproachDate >= :today ORDER BY closeApproachDate ASC")
    fun getAsteroids(today: String): LiveData<List<AsteroidDatabase>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<AsteroidDatabase>)
}

@Database(entities = [AsteroidDatabase::class], version = 1)
abstract class AsteroidRoomDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidRoomDatabase

fun getDatabase(context: Context): AsteroidRoomDatabase {
    synchronized(AsteroidRoomDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room
                    .databaseBuilder(
                        context.applicationContext,
                        AsteroidRoomDatabase::class.java,
                        "Asteroids"
                    )
                    .build()
        }
    }

    return INSTANCE
}