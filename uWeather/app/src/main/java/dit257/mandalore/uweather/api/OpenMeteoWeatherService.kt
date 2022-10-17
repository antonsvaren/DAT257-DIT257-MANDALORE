package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.time.LocalDateTime

class OpenMeteoWeatherService :
    WeatherService("https://api.open-meteo.com/v1/forecast?longitude=%s&latitude=%s&hourly=temperature_2m&daily=sunrise,sunset&timezone=auto") {
    override fun parseResponse(response: JSONObject) {
        val hourly = response.getJSONObject("hourly")
        val time = hourly.getJSONArray("time")
        val temperature2m = hourly.getJSONArray("temperature_2m")
        for (i in 0 until time.length())
            responses[LocalDateTime.parse(time.getString(i))] = temperature2m.getDouble(i)

        SUN.clear()
        val daily = response.getJSONObject("daily")
        for (key in daily.keys())
            if (key != "time")
                SUN.add(LocalDateTime.parse(daily.getJSONArray(key).getString(0)))
    }
}