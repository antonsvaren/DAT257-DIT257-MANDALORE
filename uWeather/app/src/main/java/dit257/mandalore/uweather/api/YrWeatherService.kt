package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class YrWeatherService :
    WeatherService("Yr", "https://api.met.no/weatherapi/locationforecast/2.0") {
    override fun parseResponse(response: JSONObject) {
        val timeSeries = response.getJSONObject("properties").getJSONArray("timeseries")
        for (i in 0 until timeSeries.length()) {
            val timeObject = timeSeries.getJSONObject(i)
            val details = timeObject.getJSONObject("data").getJSONObject("instant").getJSONObject("details")
            setTemperature(
                timeObject.getString("time"),
                details.getDouble("air_temperature")
            )
            uvIndex = uvIndex ?: details.getDouble("ultraviolet_index_clear_sky")
        }
    }

    override fun update(lon: String, lat: String): Future<*> {
        return request("complete?lon=$lon&lat=$lat")
    }
}