package com.example.kotlin_zadanie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

class WeatherViewModel() : ViewModel() {
    val isProgress = MutableLiveData(false)
    val response = MutableLiveData<ResponseModel>()
    val error = MutableLiveData<Throwable>()

    init {
        val url = "https://www.metaweather.com/api/location/523920/"

        isProgress.value = true
        Fuel.get(url).responseObject<ResponseModel> { _, _, result ->
            isProgress.value = false
            result.fold({
                response.value = it
            }, {
                error.value = it.exception
            })
        }
    }
}