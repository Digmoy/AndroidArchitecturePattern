package com.example.androidarchitecturepatterns.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidarchitecturepatterns.R
import com.example.androidarchitecturepatterns.app.MyApplication
import com.example.androidarchitecturepatterns.model.LoginModel
import com.example.androidarchitecturepatterns.repository.AppRepository
import com.example.androidarchitecturepatterns.utils.Event
import com.example.androidarchitecturepatterns.utils.Resource
import com.example.androidarchitecturepatterns.utils.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class LoginViewModel (app: Application, private val appRepository: AppRepository) : AndroidViewModel(app) {

    private val _loginResponse = MutableLiveData<Event<Resource<LoginModel>>>()

    val loginResponse : LiveData<Event<Resource<LoginModel>>> = _loginResponse

    fun userLoginFun(phone : String?, password : String?, device_token : String?) = viewModelScope.launch {
        userLogin(phone,password,device_token)
    }

    private suspend fun userLogin(phone: String?, password: String?, deviceToken: String?) {

        _loginResponse.postValue(Event(Resource.Loading()))

        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())){
                val response = appRepository.demoLogin(phone,password,deviceToken)
                _loginResponse.postValue(handleResponse(response))
            }
            else
            {
                _loginResponse.postValue(Event(Resource.Error(getApplication<MyApplication>().getString(
                    R.string.no_internet_connection))))
            }
        }
        catch (t : Throwable){
            when (t) {
                is IOException -> {
                    _loginResponse.postValue(
                        Event(Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.network_failure
                            )
                        ))
                    )
                }
                else -> {
                    _loginResponse.postValue(
                        Event(Resource.Error(
                            getApplication<MyApplication>().getString(
                                R.string.conversion_error)
                        )
                        ))
                }
            }
        }

    }

    private fun handleResponse(response: Response<LoginModel>): Event<Resource<LoginModel>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}