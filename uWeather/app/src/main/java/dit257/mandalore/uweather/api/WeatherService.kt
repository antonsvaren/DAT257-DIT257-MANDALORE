package dit257.mandalore.uweather.api

import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

abstract class WeatherService(val name: String, private val api: String) {
    companion object {
        val services = sequenceOf(SMHIWeatherService(), MockWeatherService())
        val cities = HashMap<String, Pair<Float, Float>>()

        init {
            cities["Gothenburg"] = Pair(11.966667F, 57.7F)
            cities["Stockholm"] = Pair(18.068611F, 59.329445F)
            cities["Malmö"] = Pair(13.035833F, 55.60583F)
        }

        fun getCities(): MutableSet<String> {
            return cities.keys
        }
    }

    val responses = arrayListOf<Map<String, Double>>()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    abstract fun parseResponse(response: String)
    abstract fun update(lon: Float, lat: Float): Future<*>?
    abstract fun getCurrentTemperature(): Double?

    fun update(city: String): Future<*>? {
        val coords = cities[city]!!
        return update(coords.first, coords.second)
    }

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
