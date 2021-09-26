package com.example.marvel.network


import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {

        val response = call.invoke()

        if (response.isSuccessful && response.body() != null)
            return response.body()!!

        var msg = ""
        val error = response.errorBody()?.string()

        error?.let {
            try {
                msg += JSONObject(it).getString("message")
            } catch (e: JSONException) {
                msg = response.message()
            }
        }

        throw ApiException(msg)
    }

}

//Exception Class
class ApiException(msg: String) : Exception(msg)