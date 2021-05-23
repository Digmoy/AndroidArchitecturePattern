package com.example.androidarchitecturepatterns.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidarchitecturepatterns.R
import com.example.androidarchitecturepatterns.app.MyApplication
import com.example.androidarchitecturepatterns.model.UpcomingModel
import com.example.androidarchitecturepatterns.repository.AppRepository
import com.example.androidarchitecturepatterns.utils.Event
import com.example.androidarchitecturepatterns.utils.Resource
import com.example.androidarchitecturepatterns.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class UpComingViewModel (app : Application, private val appRepository: AppRepository) : AndroidViewModel(app) {

    private val _upComingResponse = MutableLiveData<Event<Resource<UpcomingModel>>>()

    val upComingResponse : LiveData<Event<Resource<UpcomingModel>>> = _upComingResponse

    fun upComingFun(userID : String?) = viewModelScope.launch {
        upComing(userID)
    }

    private suspend fun upComing(userID: String?) {
        _upComingResponse.postValue(Event(Resource.Loading()))

        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())){
                val response = appRepository.demoUpComing(userID)
                _upComingResponse.postValue(handleResponse(response))
            }
            else
            {
                _upComingResponse.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                        R.string.no_internet_connection))))
            }
        }
        catch (t : Throwable)
        {
            when (t) {
                is IOException -> {
                    _upComingResponse.postValue(
                            Event(Resource.Error(
                                    getApplication<MyApplication>().getString(
                                            R.string.network_failure
                                    )
                            ))
                    )
                }
                else -> {
                    _upComingResponse.postValue(
                            Event(Resource.Error(
                                    getApplication<MyApplication>().getString(
                                            R.string.conversion_error)
                            )

                            ))
                }
            }
        }
    }

    private fun handleResponse(response: Response<UpcomingModel>): Event<Resource<UpcomingModel>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}