package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class YrWeatherService :
    WeatherService("Yr", "https://api.met.no/weatherapi/locationforecast/2.0", "air_temperature") {
    override fun parseResponse(response: JSONObject) {
        val timeSeries = response.getJSONObject("properties").getJSONArray("timeseries")
        for (i in 0 until timeSeries.length()) {
            val timeObject = timeSeries.getJSONObject(i)
            val details = timeObject.getJSONObject("data").getJSONObject("instant")
                .getJSONObject("details")
            val data = HashMap<String, Double>()
            for (name in details.keys()) data[name] = details.getDouble(name)
            addData(timeObject.getString("time"), data)
        }
    }

    override fun update(lon: Float, lat: Float): Future<*> {
        return request("compact?lon=$lon&lat=$lat")
    }
}