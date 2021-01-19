package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM AsteroidEntity WHERE closeApproachDate >= :filterDate ORDER BY closeApproachDate")
    fun getAsteroids(filterDate: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM AsteroidEntity WHERE closeApproachDate < :filterDate")
    fun clearAsteroidHistory(filterDate: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)

    @Insert
    fun insertImageOfDay(imageOfDayEntity: ImageOfDayEntity)

    @Query("DELETE FROM ImageOfDayEntity")
    fun clearImageOfDayHistory()

    @Query("SELECT * FROM ImageOfDayEntity LIMIT 1")
    fun getImageOfDay(): LiveData<ImageOfDayEntity>
}

@Database(entities = [AsteroidEntity::class, ImageOfDayEntity::class], version = 2)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabaseInstance(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }

    return INSTANCE
}