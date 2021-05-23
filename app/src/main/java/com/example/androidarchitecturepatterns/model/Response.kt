package com.example.androidarchitecturepatterns.model

data class Response(var customer_id: String, var customer_name : String,
                    var customer_email: String, var customer_phone : String,
                    var customer_alternative_phone : String, var customer_image : String,
                    var query_response_status : Boolean)
