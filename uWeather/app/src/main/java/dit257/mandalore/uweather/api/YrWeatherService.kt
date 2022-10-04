package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class YrWeatherService :
    WeatherService("Yr", "https://api.met.no/weatherapi/locationforecast/2.0") {
    override fun parseResponse(response: String) {
        val timeseries = JSONObject(response).getJSONObject("properties").getJSONArray("timeseries")
        for (i in 0 until timeseries.length()) {
            val data = HashMap<String, Double>()
            val details = timeseries.getJSONObject(i).getJSONObject("data").getJSONObject("instant")
                .getJSONObject("details")
            for (name in details.keys()) data[name] = details.getDouble(name)
            responses.add(data)
        }
    }

    override fun update(lon: Float, lat: Float): Future<*> {
        return request("compact?lon=$lon&lat=$lat")
    }

    override fun getCurrentTemperature(): Double? {
        return getOrNull(0, "air_temperature")
    }
}