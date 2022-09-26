package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class SMHIWeatherService :
    WeatherService(
        "SMHI",
        "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2"
    ) {
    override fun parseResponse(response: String) {
        val timeSeries = JSONObject(response).getJSONArray("timeSeries")
        for (i in 0 until timeSeries.length()) {
            val parameters = timeSeries.getJSONObject(i).getJSONArray("parameters")
            val response = HashMap<String, Double>()
            for (i in 0 until parameters.length()) {
                val parameter = parameters.getJSONObject(i)
                val name = parameter.getString("name")
                val value = parameter.getJSONArray("values").getDouble(0)
                response[name] = value
            }
            responses.add(response)
        }
    }

    override fun update(lon: Float, lat: Float): Future<*> {
        return request("geotype/point/lon/$lon/lat/$lat/data.json")
    }

    override fun getCurrentTemperature(): Double? {
        return responses.first()["t"]
    }
}