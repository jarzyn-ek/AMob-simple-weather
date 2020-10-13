package com.example.kotlin_zadanie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.isProgress.observe(this, Observer {
            showProgress(it)
        })

        viewModel.response.observe(this, Observer {
            showResult(it)
        })

        viewModel.error.observe(this, Observer {
            if (it == null)
                return@Observer

            showError(it)
            viewModel.error.value = null
        })
    }





    private fun showError(error: Throwable) {
        containerView.visibility = View.GONE
        Snackbar.make(
            mainView,
            "Error: ${error.message}",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun showProgress(show: Boolean) {
        progressView.visibility = if (show)
            View.VISIBLE
        else
            View.GONE
    }

    private fun showResult(model: ResponseModel) {
        containerView.visibility = View.VISIBLE

        val weather = model.forecasts.first()

        val iconUrl =
            "https://www.metaweather.com/static/img/weather/png/${weather.code}.png"
        Glide.with(this)
            .load(iconUrl)
            .into(iconWeather)

        textLocation.text = model.title
        textTempMax.text = "${weather.maxTemp.roundToInt()}"
        textTempMin.text = "${weather.minTemp.roundToInt()}"
        textAirPressure.text = "${weather.airPressure.roundToInt()} hPa"
        textWindSpeed.text = "${(weather.windSpeed*2.6).roundToInt()} km/h"
        iconWind.rotation = weather.windDirection.toFloat()
    }
}


class ResponseModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("consolidated_weather")
    val forecasts: List<Weather>
)

class Weather(
    @SerializedName("weather_state_abbr")
    val code: String,
    @SerializedName("min_temp")
    val minTemp: Double,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_direction")
    val windDirection: Double,
    @SerializedName("air_pressure")
    val airPressure: Double
)
