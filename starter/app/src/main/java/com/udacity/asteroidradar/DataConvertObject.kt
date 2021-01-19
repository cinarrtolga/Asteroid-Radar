package com.udacity.asteroidradar

import com.udacity.asteroidradar.domain.AsteroidModel
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun jsonToObject(jsonObject: JSONObject): MutableList<AsteroidModel> {
    val asteroids = ArrayList<AsteroidModel>()

    /*For filter correct json part*/
    val availableJson = jsonObject.getJSONObject("near_earth_objects")

    /* For correct day formats. Earth object nodes created from date formats.
    We will use this as a filter parameter. */
    val nextSevenDays = getFormattedWeek()

    for (day in nextSevenDays) {
        /*We take asteroids for selected date via this method.*/
        val dayAsteroid = availableJson.getJSONArray(day)

        if(dayAsteroid.length() > 0) {
            /*This loop will return each asteroid objects.*/
            for (i in 0 until dayAsteroid.length()) {
                val currentAsteroid = dayAsteroid.getJSONObject(i)

                val id = currentAsteroid.getLong("id")
                val codeName = currentAsteroid.getString("name")

                /*Taking sub json object from current object. We included index as a 0 because it is single object*/
                val closeApproachData =
                    currentAsteroid.getJSONArray("close_approach_data").getJSONObject(0)
                val closeApproachDate = closeApproachData.getString("close_approach_date")

                val absoluteMagnitude = currentAsteroid.getDouble("absolute_magnitude_h")
                val estimatedDiameter =
                    currentAsteroid.getJSONObject("estimated_diameter").getJSONObject("kilometers")
                        .getDouble("estimated_diameter_max")

                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth =
                    closeApproachData.getJSONObject("miss_distance").getDouble("astronomical")

                val isPotentiallyHazardous =
                    currentAsteroid.getBoolean("is_potentially_hazardous_asteroid")

                asteroids.add(
                    AsteroidModel(
                        id,
                        codeName,
                        closeApproachDate,
                        absoluteMagnitude,
                        estimatedDiameter,
                        relativeVelocity,
                        distanceFromEarth,
                        isPotentiallyHazardous
                    )
                )
            }
        }
    }

    return asteroids
}

private fun getFormattedWeek(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}