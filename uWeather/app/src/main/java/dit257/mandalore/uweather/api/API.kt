package dit257.mandalore.uweather.api

import org.json.JSONObject
import java.io.BufferedReader
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

abstract class API {
    companion object {
        var responses = arrayListOf<Response>()
        private val executor: ExecutorService = Executors.newCachedThreadPool()

        private fun parseResponse(response: String) {
            val timeSeries = JSONObject(response).getJSONArray("timeSeries")
            responses.clear()
            for(i in 0 until timeSeries.length()) {
                val timeSeriesObject = timeSeries.getJSONObject(i)
                responses.add(Response(timeSeriesObject.getString("validTime"), timeSeriesObject.getJSONArray("parameters")))
            }
        }

        private fun request(endpoint: String): Future<*>? {
            return executor.submit {
                URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/$endpoint").openStream()
                    .use {
                        parseResponse(it.bufferedReader().use(BufferedReader::readText))
                    }
            }
        }

        private fun geotypeRequest(endpoint: String): Future<*>? {
            return request("geotype/$endpoint")
        }

        fun pointForecast(lon: Float, lat: Float): Future<*>? {
            return geotypeRequest("point/lon/$lon/lat/$lat/data.json")
        }
    }
}
