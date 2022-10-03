package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class YrWeatherService :
    WeatherService("Yr", "https://api.met.no/weatherapi/locationforecast/2.0") {
    override fun parseResponse(response: String) {
        val properties = JSONObject(response).getJSONObject("properties").getJSONArray("timeseries")
            .getJSONObject(0).getJSONObject("data")
        for (key in arrayOf("instant", "next_1_hours", "next_6_hours", "next_12_hours")) {
            val data = HashMap<String, Double>()
            val details = properties.getJSONObject(key)
                .getJSONObject("details")
            for (name in details.keys())
                data[name] = details.getDouble(name)
            responses.add(data)
        }
    }

    override fun update(lon: Float, lat: Float): Future<*>? {
        return request("complete?lon=$lon&lat=$lat")
    }

    override fun getCurrentTemperature(): Double? {
        return responses.first()["air_temperature"]
    }
}