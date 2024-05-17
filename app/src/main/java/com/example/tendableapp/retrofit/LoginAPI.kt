package com.example.tendableapp.retrofit

import com.example.tendableapp.data.Response.InspectionResponse
import com.example.tendableapp.data.request.LoginRequest
import com.example.tendableapp.di.ApiClient
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginAPI {

    @POST("/api/register")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<Unit>

    @GET("/api/inspections/start")
    suspend fun getInspectionData(): Response<InspectionResponse>

    @POST("/api/inspections/submit")
    suspend fun submitInspectionData(@Body inspectionResponse: InspectionResponse): Response<Unit>

    companion object {
        fun getApi(): LoginAPI? {
            return ApiClient.client?.create(LoginAPI::class.java)
        }
    }
}