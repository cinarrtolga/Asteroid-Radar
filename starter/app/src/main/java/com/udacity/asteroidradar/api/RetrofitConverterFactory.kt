package com.udacity.asteroidradar.api

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.Type

//This converter for asteroid and for image of day. One of them Json object. Need to parse.
//Other one is just object. Moshi enough for convert operation. This factory metod returns
//correct factory.
class RetrofitConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        annotations?.forEach { annotation ->
            when (annotation.annotationClass) {
                StringAno::class -> return ScalarsConverterFactory.create()
                    .responseBodyConverter(type, annotations, retrofit)
                JsonAno::class -> return MoshiConverterFactory.create()
                    .responseBodyConverter(type, annotations, retrofit)
            }
        }

        return null
    }

    companion object {
        fun create() = RetrofitConverterFactory()
    }
}