package dit257.mandalore.uweather.api

import android.content.Context
import android.content.SharedPreferences

private const val selectedCityKey = "selected_city"

private fun getPreferences(context: Context): SharedPreferences {
   return context.getSharedPreferences("uWeather", Context.MODE_PRIVATE)
}

fun getSelectedCity(context: Context): String? {
    return getPreferences(context).getString(selectedCityKey, "Gothenburg")
}

fun setSelectedCity(context: Context, city: String) {
    getPreferences(context).edit().putString(selectedCityKey, city).apply()
}