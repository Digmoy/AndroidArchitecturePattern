package com.example.androidarchitecturepatterns.model

data class LoginModel(var status : Boolean, var message : String,
                      var loggedin_status : String, var customer_details : CustomerDetails)
