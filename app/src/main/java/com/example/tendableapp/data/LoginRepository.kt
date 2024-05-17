package com.example.tendableapp.data

import com.example.tendableapp.data.Response.InspectionResponse
import com.example.tendableapp.data.model.LoggedInUser
import com.example.tendableapp.data.request.LoginRequest
import com.example.tendableapp.retrofit.LoginAPI
import retrofit2.Response

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }
    suspend fun loginUser(loginRequest: LoginRequest): Response<Unit>? {
        return  LoginAPI.getApi()?.loginUser(loginRequest = loginRequest)
    }
    suspend fun inspectionData(): Response<InspectionResponse>? {
        return  LoginAPI.getApi()?.getInspectionData()
    }

    suspend fun submitData(inspectionResponse: InspectionResponse): Response<Unit>? {
        return  LoginAPI.getApi()?.submitInspectionData(inspectionResponse)
    }
}