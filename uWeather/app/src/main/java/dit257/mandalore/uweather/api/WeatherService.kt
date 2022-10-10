package dit257.mandalore.uweather.api

import org.json.JSONException
import java.io.BufferedReader
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.net.ssl.HttpsURLConnection

/**
 * A generic, unimplemented weather service.
 *
 * @property name the human-readable name for the implemented weather service.
 * @property api the base url for the API sans the request endpoints of the implemented weather
 * service.
 * @property temperatureKey the key under which temperature is stored.
 */
abstract class WeatherService(
    val name: String, private val api: String, private val temperatureKey: String
) {
    companion object {
        val services = sequenceOf(SMHIWeatherService(), YrWeatherService())
        val cities = HashMap<String, Pair<Float, Float>>()

        init {
            cities["Gothenburg"] = Pair(11.9667F, 57.7F)
            cities["Stockholm"] = Pair(18.0686F, 59.3295F)
            cities["Malmö"] = Pair(13.0358F, 55.6058F)
        }

        /**
         * Gets a collection of supported city names in no particular order.
         *
         * @return the collection of supported city names.
         */
        fun getCities(): Set<String> {
            return cities.keys
        }
    }

    private val responses = TreeMap<LocalDateTime, Map<String, Double>>()
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * Parses a JSON response from the API into [responses].
     *
     * @param response the JSON response from the weather API.
     */
    abstract fun parseResponse(response: JSONObject)

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
     * Stores the data in [responses] after parsing the given time into a [LocalDateTime] object.
     *
     * @param time the raw zoned ISO time for when the data becomes valid.
     * @param data the data from the API.
     */
    fun addData(time: String, data: Map<String, Double>) {
        responses[LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME)] = data
    }

    /**
     * Gets the temperature from the API for the current time if it exists.
     *
     * @return the temperature from the API for the current time, or null.
     */
    fun getCurrentTemperature(): Double? {
        return responses.lowerEntry(LocalDateTime.now(ZoneOffset.UTC))?.value?.get(temperatureKey)
    }

    /**
     * Calls [update] after converting the given city name to coordinates.
     *
     * @param city the English exonym for the city at which to get the weather.
     * @return the [Future] for and parsing the result, or null.
     */
    fun update(city: String): Future<*>? {
        val (lon, lat) = cities[city]!!
        return update(lon, lat)
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
            val connection = URL("$api/$endpoint").openConnection() as HttpsURLConnection
            try {
                // Needed for yr.no; doesn't hurt for other services
                connection.setRequestProperty(
                    "User-Agent", "uWeather/1.0 github.com/antonsvaren/DAT257-DIT257-MANDALORE"
                )
                connection.inputStream.bufferedReader()
                    .use { parseResponse(JSONObject(it.readText())) }
            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }
    }
}
