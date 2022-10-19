package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.lang.Double.max
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class YrWeatherService :
    WeatherService("https://api.met.no/weatherapi/locationforecast/2.0/complete?lon=%s&lat=%s") {
    override fun parseResponse(response: JSONObject) {
        val morning = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        val midnight = morning.plusDays(1)

        val timeSeries = response.getJSONObject("properties").getJSONArray("timeseries")
        for (i in 0 until timeSeries.length()) {
            val timeObject = timeSeries.getJSONObject(i)
            val data = timeObject.getJSONObject("data")
            val details = data.getJSONObject("instant").getJSONObject("details")
            val time = setTemperature(
                timeObject.getString("time"), details.getDouble("air_temperature")
            )
            WEATHER[time] =
                data.getJSONObject("next_1_hours").getJSONObject("summary").getString("symbol_code")
            if (!time.isBefore(morning) && time.isBefore(midnight)) UV_INDEX =
                max(UV_INDEX ?: 0.0, details.getDouble("ultraviolet_index_clear_sky"))
        }
    }
}