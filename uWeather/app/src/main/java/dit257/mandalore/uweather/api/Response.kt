package dit257.mandalore.uweather.api

import org.json.JSONArray

data class Response(val validTime: String): HashMap<String, Double>() {
    constructor(validTime: String, parameters: JSONArray) : this(validTime) {
        for (i in 0 until parameters.length()) {
            val parameter = parameters.getJSONObject(i)
            val name = parameter.getString("name")
            val value = parameter.getJSONArray("values").getDouble(0)
            this[name] = value
        }
    }
}