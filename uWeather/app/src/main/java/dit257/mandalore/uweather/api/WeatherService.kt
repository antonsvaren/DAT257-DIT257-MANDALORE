package dit257.mandalore.uweather.api

import java.io.BufferedReader
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

abstract class WeatherService(private val api: String) {
    companion object {
        val services = sequenceOf<WeatherService>(SMHIWeatherService())
        private val executor: ExecutorService = Executors.newCachedThreadPool()
    }
    
    val responses = arrayListOf<Response>()

    abstract fun parseResponse(response: String)
    abstract fun update(lon: Float, lat: Float): Future<*>?

    fun getCurrentTemperature(): Double? {
        return responses.first()["t"]
    }

    fun request(endpoint: String): Future<*> {
        return executor.submit {
            URL("$api/$endpoint").openStream()
                .use {
                    parseResponse(it.bufferedReader().use(BufferedReader::readText))
                }
        }
    }
}
