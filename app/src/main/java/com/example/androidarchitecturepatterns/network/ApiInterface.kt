package com.example.androidarchitecturepatterns.network

import com.example.androidarchitecturepatterns.model.LoginModel
import com.example.androidarchitecturepatterns.model.UpcomingModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {

    @FormUrlEncoded
    @POST("user/login_customer")
    suspend fun userLogin(
        @Field("phone") phone: String?,
        @Field("password") password: String?,
        @Field("device_token") device_token: String?
        ): Response<LoginModel>


    @FormUrlEncoded
    @POST("user/all_upcoming_products")
    suspend fun upComingProduct (@Field("customer_id") customer_id : String?) : Response<UpcomingModel>
}