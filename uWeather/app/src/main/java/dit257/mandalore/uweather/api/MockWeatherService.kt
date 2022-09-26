package dit257.mandalore.uweather.api

import java.util.concurrent.Future

class MockWeatherService : WeatherService("Mock", "") {
    override fun parseResponse(response: String) {}

    override fun update(lon: Float, lat: Float): Future<*>? {
        return request("")
    }

    override fun getCurrentTemperature(): Double? {
        return 9214972148.12
    }
}