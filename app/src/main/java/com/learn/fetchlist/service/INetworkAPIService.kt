package com.learn.fetchlist.service

import com.learn.fetchlist.data.HiringList
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface INetworkAPIService {

    @GET("hiring.json")
    suspend fun getFetchHiringList(): Response<List<HiringList>>

    companion object {

        fun create(baseUrl: String): INetworkAPIService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(INetworkAPIService::class.java)
        }
    }
}