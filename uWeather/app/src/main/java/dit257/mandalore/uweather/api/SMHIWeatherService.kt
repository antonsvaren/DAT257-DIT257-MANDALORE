package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class SMHIWeatherService : WeatherService(
    "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/%s/lat/%s/data.json"
) {
    override fun parseResponse(response: JSONObject) {
        val timeSeries = response.getJSONArray("timeSeries")
        var time = response.getString("referenceTime")
        for (i in 0 until timeSeries.length()) {
            val timeObject = timeSeries.getJSONObject(i)
            val parameters = timeObject.getJSONArray("parameters")
            for (j in 0 until parameters.length()) {
                val parameter = parameters.getJSONObject(j)
                if (parameter.getString("name").equals("t")) {
                    setTemperature(time, parameter.getJSONArray("values").getDouble(0))
                    break
                }
            }
            time = timeObject.getString("validTime")
        }
    }
}