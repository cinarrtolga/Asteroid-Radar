package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabaseInstance
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    companion object {
        const val REFRESH_ASTEROID = "RefreshAsteroid"
    }

    override suspend fun doWork(): Result {
        val database = getDatabaseInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.clearAsteroidHistory()
            repository.refreshAsteroids()
            repository.refreshImageOfDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}