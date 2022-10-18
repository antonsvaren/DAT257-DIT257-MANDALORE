package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.time.ZoneOffset

class OpenMeteoWeatherService :
    WeatherService("https://api.open-meteo.com/v1/forecast?longitude=%s&latitude=%s&hourly=temperature_2m&daily=sunrise,sunset&timezone=auto") {
    override fun parseResponse(response: JSONObject) {
        val offsetID = ZoneOffset.ofTotalSeconds(response.getInt("utc_offset_seconds")).id
        val hourly = response.getJSONObject("hourly")
        val time = hourly.getJSONArray("time")
        val temperature2m = hourly.getJSONArray("temperature_2m")
        for (i in 0 until time.length()) setTemperature(
            time.getString(i) + offsetID, temperature2m.getDouble(i)
        )

        val daily = response.getJSONObject("daily")
        SUNTIMES = arrayOf("sunrise", "sunset").map {
            parseTime(
                daily.getJSONArray(it).getString(0) + offsetID
            )
        }
    }
}