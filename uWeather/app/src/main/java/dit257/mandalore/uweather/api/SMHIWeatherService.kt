package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class SMHIWeatherService :
    WeatherService("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2") {
    override fun parseResponse(response: String) {
        val timeSeries = JSONObject(response).getJSONArray("timeSeries")
        responses.clear()
        for (i in 0 until timeSeries.length()) {
            val timeSeriesObject = timeSeries.getJSONObject(i)
            responses.add(
                Response(
                    timeSeriesObject.getString("validTime"),
                    timeSeriesObject.getJSONArray("parameters")
                )
            )
        }
    }

    override fun update(lon: Float, lat: Float): Future<*> {
        return request("geotype/point/lon/$lon/lat/$lat/data.json")
    }
}