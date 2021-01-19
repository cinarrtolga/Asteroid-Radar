package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.domain.AsteroidModel
import com.udacity.asteroidradar.domain.ImageOfDayModel
import com.udacity.asteroidradar.repository.AsteroidRepository

enum class AsteroidFilter { DAILY, WEEKLY, ALL }

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabaseInstance(application)
    private val asteroidRepository = AsteroidRepository(database)

    val asteroidList: LiveData<MutableList<AsteroidModel>>
        get() = asteroidRepository.asteroids

    val imageOfDay: LiveData<ImageOfDayModel>
        get() = asteroidRepository.imageOfDay

    val currentAsteroid = MutableLiveData<AsteroidModel>()

    fun navigationCompleted() {
        currentAsteroid.value = null
    }

    fun filterAsteroids(filter: AsteroidFilter): List<AsteroidModel>? {
        return when(filter) {
            AsteroidFilter.DAILY -> asteroidList.value?.filter { it.closeApproachDate == asteroidRepository.getToday() }
            AsteroidFilter.WEEKLY -> asteroidList.value
            else -> asteroidList.value
        }
    }
}