package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class SMHIWeatherService : WeatherService(
    "SMHI", "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2"
) {
    override fun parseResponse(response: String) {
        val timeSeries = JSONObject(response).getJSONArray("timeSeries")
        for (i in 0 until timeSeries.length()) {
            val parameters = timeSeries.getJSONObject(i).getJSONArray("parameters")
            val data = HashMap<String, Double>()
            for (j in 0 until parameters.length()) {
                val parameter = parameters.getJSONObject(j)
                val name = parameter.getString("name")
                val value = parameter.getJSONArray("values").getDouble(0)
                data[name] = value
            }
            responses.add(data)
        }
    }

    override fun update(lon: Float, lat: Float): Future<*> {
        return request("geotype/point/lon/$lon/lat/$lat/data.json")
    }

    override fun getCurrentTemperature(): Double? {
        return getOrNull(0, "t")
    }
}