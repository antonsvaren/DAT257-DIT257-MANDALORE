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
 * @property endpoint the endpoint to call to update this service.
 */
abstract class WeatherService(private val endpoint: String) {
    val temperatures = TreeMap<LocalDateTime, Double>()

    /**
     * Parses a JSON response from the API into [temperatures].
     *
     * @param response the JSON response from the weather API.
     */
    abstract fun parseResponse(response: JSONObject)

    /**
     * Sets the data in [temperatures] after parsing the given time into a [LocalDateTime] object.
     *
     * @param time the raw zoned ISO time for when the data becomes valid.
     * @param temperature the temperature from the API.
     * @return the parsed [LocalDateTime] object.
     */
    fun setTemperature(time: String, temperature: Double): LocalDateTime {
        val key = LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        temperatures[parseTime(time)] = temperature
        return key
    }

    /**
     * Sends a request asynchronously to the given endpoint and calls [parseResponse] with the
     * result.
     *
     * @param args the longitude and latitude [String]s passed into [endpoint].
     * @return the [Future] for calling the API and parsing the result.
     */
    fun update(vararg args: Any?): Future<*> {
        return EXECUTOR.submit {
            val connection = URL(endpoint.format(*args)).openConnection() as HttpsURLConnection
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

/**
 * Calls [WeatherService.update] on all [SERVICES] after converting the given city name to coordinates.
 *
 * @param city the English exonym for the city at which to get the weather.
 */
fun updateAll(city: String) {
    val (lon, lat) = CITIES[city] ?: return
    SERVICES.map { it.update(lon, lat) }.forEach { it.get() }
}

/**
 * Gets the current time, zoned for UTC.
 *
 * @return the current time in UTC offset.
 */
fun getCurrentTime(): LocalDateTime {
    return LocalDateTime.now(ZoneOffset.UTC)
}

/**
 * Gets all non-null temperatures across every service.
 *
 * @param time the time with which to search for valid temperatures.
 * @return all non-null temperatures across every service.
 */
fun getTemperatures(time: LocalDateTime): Sequence<Double> {
    return SERVICES.map { getMapInfo(it.temperatures, time) }.filterNotNull()
}

/**
 * Parses the current zoned time into a [LocalDateTime] object.
 *
 * @param time the raw zoned ISO time.
 * @return the parsed [LocalDateTime] object.
 */
fun parseTime(time: String): LocalDateTime {
    return LocalDateTime.parse(time, DateTimeFormatter.ISO_ZONED_DATE_TIME)
}

fun <T> getMapInfo(map: TreeMap<LocalDateTime, T>, time: LocalDateTime): T? {
    return map.lowerEntry(time)?.value
}

var UV_INDEX: Double? = null
var SUNTIMES = emptyList<LocalDateTime>()
val WEATHER = TreeMap<LocalDateTime, String>()
val LEGEND = HashMap<String, String>()

val SERVICES = sequenceOf(OpenMeteoWeatherService(), SMHIWeatherService(), YrWeatherService())
val EXECUTOR = Executors.newFixedThreadPool(SERVICES.count())!!
val CITIES = mapOf(
    Pair("Alings??s", Pair("12.5331", "57.93")),
    Pair("Arboga", Pair("15.8386", "59.3939")),
    Pair("Arvika", Pair("12.5914", "59.6542")),
    Pair("Askersund", Pair("14.9028", "58.8797")),
    Pair("Avesta", Pair("16.1683", "60.1456")),
    Pair("Boden", Pair("21.6906", "65.8256")),
    Pair("Bolln??s", Pair("16.3947", "61.3481")),
    Pair("Borgholm", Pair("16.6558", "56.8792")),
    Pair("Borl??nge", Pair("15.4364", "60.4856")),
    Pair("Bor??s", Pair("12.9403", "57.7211")),
    Pair("Djursholm", Pair("18.0875", "59.3972")),
    Pair("Eksj??", Pair("14.9703", "57.6669")),
    Pair("Enk??ping", Pair("17.0764", "59.6356")),
    Pair("Eskilstuna", Pair("16.5097", "59.3708")),
    Pair("Esl??v", Pair("13.3039", "55.8392")),
    Pair("Fagersta", Pair("15.7933", "60.0042")),
    Pair("Falkenberg", Pair("12.4911", "56.9053")),
    Pair("Falk??ping", Pair("13.5531", "58.175")),
    Pair("Falsterbo", Pair("12.8333", "55.3833")),
    Pair("Falun", Pair("15.6311", "60.6072")),
    Pair("Filipstad", Pair("14.1667", "59.7167")),
    Pair("Flen", Pair("16.5833", "59.05")),
    Pair("Gothenburg", Pair("11.9667", "57.7")),
    Pair("Gr??nna", Pair("14.4667", "58.0167")),
    Pair("G??vle", Pair("17.1417", "60.6747")),
    Pair("Hagfors", Pair("13.65", "60.0333")),
    Pair("Halmstad", Pair("12.8572", "56.6739")),
    Pair("Haparanda", Pair("24.1333", "65.8333")),
    Pair("Hedemora", Pair("15.9872", "60.2769")),
    Pair("Helsingborg", Pair("12.7167", "56.05")),
    Pair("Hjo", Pair("14.2833", "58.3")),
    Pair("Hudiksvall", Pair("17.1167", "61.7333")),
    Pair("Huskvarna", Pair("14.2667", "57.8")),
    Pair("H??rn??sand", Pair("17.9411", "62.6361")),
    Pair("H??ssleholm", Pair("13.7667", "56.1667")),
    Pair("H??gan??s", Pair("12.5667", "56.2")),
    Pair("J??nk??ping", Pair("14.1606", "57.7828")),
    Pair("Kalmar", Pair("16.3628", "56.6614")),
    Pair("Karlshamn", Pair("14.85", "56.1667")),
    Pair("Karlskoga", Pair("14.5167", "59.3333")),
    Pair("Karlskrona", Pair("15.5861", "56.1608")),
    Pair("Karlstad", Pair("13.5042", "59.3783")),
    Pair("Katrineholm", Pair("16.2", "59.0")),
    Pair("Kiruna", Pair("20.3028", "67.8489")),
    Pair("Kramfors", Pair("17.8", "62.9333")),
    Pair("Kristianstad", Pair("14.1567", "56.0294")),
    Pair("Kristinehamn", Pair("14.1167", "59.3")),
    Pair("Kumla", Pair("15.1333", "59.1167")),
    Pair("Kungsbacka", Pair("12.0667", "57.4833")),
    Pair("Kung??lv", Pair("11.9667", "57.8667")),
    Pair("K??ping", Pair("15.9833", "59.5167")),
    Pair("Laholm", Pair("13.0333", "56.5167")),
    Pair("Landskrona", Pair("12.8311", "55.8706")),
    Pair("Liding??", Pair("18.15", "59.3667")),
    Pair("Lidk??ping", Pair("13.1833", "58.5")),
    Pair("Lindesberg", Pair("15.25", "59.5833")),
    Pair("Link??ping", Pair("15.6253", "58.4158")),
    Pair("Ljungby", Pair("13.9333", "56.8333")),
    Pair("Ludvika", Pair("15.1833", "60.1333")),
    Pair("Lule??", Pair("22.1539", "65.5844")),
    Pair("Lund", Pair("13.195", "55.7039")),
    Pair("Lycksele", Pair("18.6667", "64.6")),
    Pair("Lysekil", Pair("11.4333", "58.2833")),
    Pair("Malm??", Pair("13.0358", "55.6058")),
    Pair("Mariefred", Pair("17.2167", "59.2667")),
    Pair("Mariestad", Pair("13.8167", "58.7")),
    Pair("Marstrand", Pair("11.5833", "57.8833")),
    Pair("Mj??lby", Pair("15.1167", "58.3333")),
    Pair("Motala", Pair("15.0333", "58.5333")),
    Pair("Nacka", Pair("18.1639", "59.31")),
    Pair("Nora", Pair("15.0333", "59.5167")),
    Pair("Norrk??ping", Pair("16.2", "58.6")),
    Pair("Norrt??lje", Pair("18.7", "59.7667")),
    Pair("Nybro", Pair("15.9", "56.7333")),
    Pair("Nyk??ping", Pair("17.0086", "58.7531")),
    Pair("Nyn??shamn", Pair("17.95", "58.9")),
    Pair("N??ssj??", Pair("14.6833", "57.65")),
    Pair("Oskarshamn", Pair("16.45", "57.265")),
    Pair("Oxel??sund", Pair("17.1167", "58.6667")),
    Pair("Pite??", Pair("21.5", "65.3333")),
    Pair("Ronneby", Pair("15.2833", "56.2")),
    Pair("Sala", Pair("16.6", "59.9167")),
    Pair("Sandviken", Pair("16.7833", "60.6167")),
    Pair("Sigtuna", Pair("17.7167", "59.6167")),
    Pair("Simrishamn", Pair("14.35", "55.55")),
    Pair("Skan??r", Pair("12.85", "55.4167")),
    Pair("Skan??r med Falsterbo", Pair("12.85", "55.4")),
    Pair("Skara", Pair("13.4333", "58.3833")),
    Pair("Skellefte??", Pair("20.95", "64.75")),
    Pair("Sk??nninge", Pair("15.0833", "58.4")),
    Pair("Sk??vde", Pair("13.85", "58.3833")),
    Pair("Sollefte??", Pair("17.2667", "63.1667")),
    Pair("Solna", Pair("18.0", "59.35")),
    Pair("Str??ngn??s", Pair("17.0333", "59.3667")),
    Pair("Str??mstad", Pair("11.1667", "58.9333")),
    Pair("Sundbyberg", Pair("17.9667", "59.3667")),
    Pair("Sundsvall", Pair("17.3167", "62.4")),
    Pair("S??ffle", Pair("12.9333", "59.1333")),
    Pair("S??ter", Pair("15.75", "60.35")),
    Pair("S??vsj??", Pair("14.6667", "57.4")),
    Pair("S??derhamn", Pair("17.0833", "61.3")),
    Pair("S??derk??ping", Pair("16.3167", "58.4833")),
    Pair("S??dert??lje", Pair("17.6281", "59.1958")),
    Pair("S??lvesborg", Pair("14.5753", "56.0442")),
    Pair("Tidaholm", Pair("13.95", "58.1833")),
    Pair("Torsh??lla", Pair("16.4667", "59.4167")),
    Pair("Tran??s", Pair("14.9667", "58.0333")),
    Pair("Trelleborg", Pair("13.1667", "55.3667")),
    Pair("Trollh??ttan", Pair("12.2892", "58.2828")),
    Pair("Trosa", Pair("17.55", "58.9")),
    Pair("Uddevalla", Pair("11.9167", "58.35")),
    Pair("Ulricehamn", Pair("13.4167", "57.7833")),
    Pair("Ume??", Pair("20.2639", "63.825")),
    Pair("Uppsala", Pair("17.6447", "59.8581")),
    Pair("Vadstena", Pair("14.9", "58.45")),
    Pair("Varberg", Pair("12.2167", "57.1167")),
    Pair("Vaxholm", Pair("18.3167", "59.4167")),
    Pair("Vetlanda", Pair("15.0667", "57.4333")),
    Pair("Vimmerby", Pair("15.85", "57.6667")),
    Pair("Visby", Pair("18.2992", "57.6347")),
    Pair("V??nersborg", Pair("12.325", "58.3806")),
    Pair("V??rnamo", Pair("14.0333", "57.1833")),
    Pair("V??stervik", Pair("16.6333", "57.75")),
    Pair("V??ster??s", Pair("16.5528", "59.6161")),
    Pair("V??xj??", Pair("14.8092", "56.8769")),
    Pair("Ystad", Pair("13.8333", "55.4167")),
    Pair("??m??l", Pair("12.7", "59.05")),
    Pair("??ngelholm", Pair("12.85", "56.25")),
    Pair("??rebro", Pair("15.2075", "59.2739")),
    Pair("??regrund", Pair("18.4333", "60.3333")),
    Pair("??rnsk??ldsvik", Pair("18.7156", "63.2908")),
    Pair("??stersund", Pair("14.6358", "63.1792")),
    Pair("??sthammar", Pair("18.3667", "60.2667")),
    Pair("Stockholm", Pair("18.0686", "59.3294"))
)