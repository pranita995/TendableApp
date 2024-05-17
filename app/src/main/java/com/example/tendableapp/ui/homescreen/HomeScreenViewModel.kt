package com.example.tendableapp.ui.homescreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tendableapp.data.LoginRepository
import com.example.tendableapp.data.Response.InspectionResponse
import com.example.tendableapp.di.ApiClient
import com.example.tendableapp.retrofit.LoginAPI
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomeScreenViewModel(
    private val loginRepository: LoginRepository
) : ViewModel() {
    // Gson instance for JSON serialization
    private val gson = Gson()
    private val _inspectionDataSaved = MutableLiveData<Boolean>()
    val inspectionDataSaved: LiveData<Boolean> = _inspectionDataSaved
    val _notifySucess = MutableLiveData<Boolean>()
    val _dataFetch = MutableLiveData<InspectionResponse>()
    fun fetchAndSaveInspectionData() {
        viewModelScope.launch {
            try {
                val response: Response<InspectionResponse> = loginRepository.inspectionData()!!

                if (response.isSuccessful) {
                    val inspectionResponse: InspectionResponse? = response.body()
                    if (inspectionResponse != null) {
                        print("Reponse body")
                        _dataFetch.postValue(response.body())
                    } else {
                        // Handle null response body
                    }
                } else {
                    // Handle unsuccessful response (e.g., error response)
                    val errorBody = response.errorBody()
                    // Process error body or handle error accordingly
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("HomeViewModel", "Error fetching inspection data: ${e.message}")
            }
        }
    }

    suspend fun submitInspectionData(inspectionData: InspectionResponse) {
        val inspectionJson = gson.toJson(inspectionData)

        val response = loginRepository.submitData(inspectionData)
        Log.d("TAG", "submitInspectionData: "+inspectionJson)
        when (response!!.code()) {
            200 -> {
                println("successful submitted")
                _notifySucess.postValue(true)  // Notify success
            }
            400 -> {
                println("unsuccessful: Missing fields in the JSON")
            }
            401 -> {
                println("unsuccessful: ")
            }
            else -> {
                println("Unexpected response code: ${response.code()}")
            }
        }
    }

}