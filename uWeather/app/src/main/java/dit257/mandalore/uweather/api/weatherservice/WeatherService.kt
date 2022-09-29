package dit257.mandalore.uweather.api.weatherservice

import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

abstract class WeatherService(val name: String, private val api: String) {
    companion object {
        val services = sequenceOf(SMHIWeatherService(), MockWeatherService())
    }

    val responses = arrayListOf<Map<String, Double>>()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    abstract fun parseResponse(response: String)
    abstract fun update(lon: Float, lat: Float): Future<*>?
    abstract fun getCurrentTemperature(): Double?

    fun request(endpoint: String): Future<*> {
        responses.clear()
        return executor.submit {
            try {
                URL("$api/$endpoint").openStream()
                    .use {
                        parseResponse(it.bufferedReader().use(BufferedReader::readText))
                    }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
