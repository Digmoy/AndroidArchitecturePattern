package com.example.androidarchitecturepatterns.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidarchitecturepatterns.repository.AppRepository

class ViewModelProviderFactory(val application: Application, val appRepository: AppRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
        {
            return LoginViewModel(application,appRepository) as T
        }
        if (modelClass.isAssignableFrom(UpComingViewModel::class.java))
        {
            return UpComingViewModel(application,appRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}