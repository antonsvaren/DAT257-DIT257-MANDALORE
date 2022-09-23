package dit257.mandalore.uweather

import org.json.JSONObject
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

abstract class API {
    companion object {
        var current = JSONObject()
        private val executor: ExecutorService = Executors.newCachedThreadPool()

        private fun request(endpoint: String): Future<*>? {
            return executor.submit {
                current =
                    JSONObject(URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/$endpoint").openStream()
                        .use {
                            it.bufferedReader().readText()
                        })
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
