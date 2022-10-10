package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.util.concurrent.Future

class MockWeatherService : WeatherService("Mock", "") {
    override fun parseResponse(response: JSONObject) {}

    override fun update(lon: Float, lat: Float): Future<*>? {
        return null
    }

    override fun getCurrentTemperature(): Double {
        return 12.0
    }
}