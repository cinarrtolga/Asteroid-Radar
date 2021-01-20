package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.AsteroidModel
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.jsonToObject
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val database: AsteroidsDatabase) {
    val asteroids: LiveData<MutableList<AsteroidModel>> =
        Transformations.map(database.asteroidDao.getAsteroids(getToday())) {
            it.asDomainModel()
        }

    val imageOfDay = Transformations.map(database.asteroidDao.getImageOfDay()) {
        it?.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        try {
            val asteroids: MutableList<AsteroidModel> =
                jsonToObject(
                    JSONObject(
                        AsteroidApi.retrofitService.getMarsProperties(
                            getToday(),
                            Constants.API_KEY
                        )
                    )
                )

            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        } catch (ex: Exception) {
            Log.i("warning", "For offline mode.")
        }
    }

    suspend fun refreshImageOfDay() {
        try {
            val imageOfDay =
                AsteroidApi.retrofitService.getImageOfDay(Constants.API_KEY).asDatabaseModel()

            database.asteroidDao.insertImageOfDay(imageOfDay)
        } catch (ex: Exception) {
            Log.i("warning", "For offline mode")
        }
    }

    fun clearAsteroidHistory() {
        database.asteroidDao.clearAsteroidHistory(getToday())
    }

    fun getToday(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        return dateFormat.format(currentTime)
    }
}