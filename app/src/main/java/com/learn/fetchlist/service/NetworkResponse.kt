package com.learn.fetchlist.service

sealed class NetworkResponse {

    data class Success(val data: Any, val callType: Int = 0) : NetworkResponse()

    data class Failure(
        val throwable: Throwable, val callTypeResponseCode: Int = 0,
        val jobFailureCode: Int = 0
    ) : NetworkResponse()
}