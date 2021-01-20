package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.domain.ImageOfDayModel
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention()
internal annotation class StringAno

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention()
internal annotation class JsonAno

private val retrofit = Retrofit.Builder()
    .addConverterFactory(RetrofitConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {
    //I did not use end date in here because it's already +7 days as a default.
    @StringAno
    @GET("neo/rest/v1/feed")
    suspend fun getMarsProperties(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): String

    @JsonAno
    @GET("planetary/apod")
    suspend fun getImageOfDay(@Query("api_key") apiKey: String): ImageOfDayModel
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}