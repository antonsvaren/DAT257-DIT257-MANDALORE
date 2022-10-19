package dit257.mandalore.uweather.api

import org.json.JSONObject

class WeatherLegendService :
    WeatherService("https://api.met.no/weatherapi/weathericon/2.0/legends") {
    override fun parseResponse(response: JSONObject) {
        for (key in response.keys()) LEGEND[key] = response.getJSONObject(key).getString("desc_en")
    }
}