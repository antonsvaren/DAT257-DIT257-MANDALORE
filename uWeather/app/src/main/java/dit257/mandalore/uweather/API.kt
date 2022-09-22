package dit257.mandalore.uweather

import java.net.URL
import java.util.concurrent.Executors

fun request(endpoint: String, command: (String) -> Any) {
    Executors.newSingleThreadExecutor().execute {
        try {
            URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/$endpoint").openStream()
                .use {
                    command(it.bufferedReader().readText())
                }
        } catch(e: Exception) {
            command(e.message?: e.stackTraceToString())
        }
    }
}

fun geotypeRequest(endpoint: String, command: (String) -> Any) {
    request("geotype/$endpoint", command)
}

fun pointForecast(lon: Float, lat: Float, command: (String) -> Any) {
    geotypeRequest("lon/$lon/lat/$lat/data.json", command)
}
