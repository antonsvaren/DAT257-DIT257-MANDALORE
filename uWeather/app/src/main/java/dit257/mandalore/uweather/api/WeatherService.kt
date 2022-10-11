package dit257.mandalore.uweather.api

import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.net.ssl.HttpsURLConnection

/**
 * A generic, unimplemented weather service.
 *
 * @property name the human-readable name for the implemented weather service.
 * @property api the base url for the API sans the request endpoints of the implemented weather
 * service.
 */
abstract class WeatherService(
    val name: String, private val api: String
) {
    companion object {
        val services = sequenceOf(MockWeatherService(), SMHIWeatherService(), YrWeatherService())
        val executors = services.associate { it.name to Executors.newSingleThreadExecutor() }

        /**
         * Calls [update] on all [services] after converting the given city name to coordinates.
         *
         * @param city the English exonym for the city at which to get the weather.
         */
        fun updateAll(city: String) {
            val (lon, lat) = cities[city]!!
            services.map { it.update(lon, lat) }.forEach { it?.get() }
        }
    }

    private val responses = TreeMap<LocalDateTime, Double>()

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
    abstract fun update(lon: String, lat: String): Future<*>?

    /**
     * Gets the temperature from the API for the current time if it exists. Override this for mocks.
     *
     * @return the temperature from the API for the current time, or null.
     */
    open fun getCurrentTemperature(): Double? {
        return getTemperature(LocalDateTime.now(ZoneOffset.UTC))
    }

    /**
     * Stores the data in [responses] after parsing the given time into a [LocalDateTime] object.
     *
     * @param time the raw zoned ISO time for when the data becomes valid.
     * @param temperature the temperature from the API.
     */
    fun addData(time: String, temperature: Double) {
        responses[LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME)] = temperature
    }

    /**
     * Gets the temperature from the API for the given time if it exists.
     *
     * @param time the time to get the temperature for.
     * @return the temperature from the API for the given time, or null.
     */
    fun getTemperature(time: LocalDateTime): Double? {
        return responses.lowerEntry(time)?.value
    }

    /**
     * Sends a request asynchronously to the given endpoint and calls [parseResponse] with the
     * result.
     *
     * @param endpoint the endpoint of the request added to [api].
     * @return the [Future] for calling the API and parsing the result.
     */
    fun request(endpoint: String): Future<*>? {
        responses.clear()
        return executors[name]?.submit {
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
