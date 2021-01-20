package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    //This method for Main Fragment item listing.
    @Query("SELECT * FROM AsteroidEntity WHERE closeApproachDate >= :filterDate ORDER BY closeApproachDate")
    fun getAsteroids(filterDate: String): LiveData<List<AsteroidEntity>>

    //This method is removing old asteroids. Before today.
    @Query("DELETE FROM AsteroidEntity WHERE closeApproachDate < :filterDate")
    fun clearAsteroidHistory(filterDate: String)

    //For caching. After retrofit, inserting all asteroids.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)

    //Inserting image of day to database.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertImageOfDay(imageOfDayEntity: ImageOfDayEntity)

    //Removing old images.
    @Query("DELETE FROM ImageOfDayEntity")
    fun clearImageOfDayHistory()

    //For main fragment header. Image of day area.
    @Query("SELECT * FROM ImageOfDayEntity ORDER BY date DESC LIMIT 1")
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