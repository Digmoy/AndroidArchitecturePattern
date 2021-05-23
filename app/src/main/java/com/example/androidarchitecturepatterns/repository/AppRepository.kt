package com.example.androidarchitecturepatterns.repository


import com.example.androidarchitecturepatterns.network.RetrofitClient

class AppRepository {

    suspend fun demoLogin (phone : String?, password : String?, device_token : String?) =
        RetrofitClient.apiInterface.userLogin(phone, password, device_token)

    suspend fun demoUpComing (userID : String?) = RetrofitClient.apiInterface.upComingProduct(userID)

}