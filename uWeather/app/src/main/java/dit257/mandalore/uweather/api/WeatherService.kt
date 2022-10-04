package dit257.mandalore.uweather.api

import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * A generic, unimplemented weather service.
 *
 * @property name the human-readable name for the implemented weather service.
 * @property api the base url for the API sans the request endpoints of the implemented weather
 * service.
 */
abstract class WeatherService(val name: String, private val api: String) {
    companion object {
        val services = sequenceOf(SMHIWeatherService(), MockWeatherService())

        /**
         * A map of supported cities in Sweden to their coordinates, using the English exonyms. The
         * coordinates are longitude first, latitude second.
         */
        val cities = HashMap<String, Pair<Float, Float>>()

        init {
            cities["Gothenburg"] = Pair(11.966667F, 57.7F)
            cities["Stockholm"] = Pair(18.068611F, 59.329445F)
            cities["Malmö"] = Pair(13.035833F, 55.60583F)
        }

        /**
         * Gets a collection of supported city names in no particular order.
         *
         * @return the collection of supported city names.
         */
        fun getCities(): MutableSet<String> {
            return cities.keys
        }
    }

    val responses = arrayListOf<Map<String, Double>>()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * Parses a raw response from the API into [responses].
     *
     * @param response the raw response from the weather API.
     */
    abstract fun parseResponse(response: String)

    /**
     * Requests the latest weather from the weather API asynchronously at the given location and
     * calls [parseResponse] with the result.
     *
     * @param lon the longitude at which to get the weather.
     * @param lat the latitude at which to get the weather.
     * @return the [Future] for and parsing the result, or null
     */
    abstract fun update(lon: Float, lat: Float): Future<*>?

    /**
     * Gets the latest temperature information from the API if it exists.
     *
     * @return the latest temperature information from the API, or null.
     */
    abstract fun getCurrentTemperature(): Double?

    /**
     * Calls [update] after converting the given city name to coordinates.
     *
     * @param city the English exonym for the city at which to get the weather.
     * @return the [Future] for and parsing the result, or null.
     */
    fun update(city: String): Future<*>? {
        val coords = cities[city]!!
        return update(coords.first, coords.second)
    }

    /**
     * Sends a request asynchronously to the given endpoint and calls [parseResponse] with the
     * result.
     *
     * @param endpoint the endpoint of the request added to [api].
     * @return the [Future] for calling the API and parsing the result.
     */
    fun request(endpoint: String): Future<*> {
        responses.clear()
        return executor.submit {
            try {
                URL("$api/$endpoint").openStream().use {
                    parseResponse(it.bufferedReader().use(BufferedReader::readText))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
