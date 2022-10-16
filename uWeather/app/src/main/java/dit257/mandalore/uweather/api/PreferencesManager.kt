package dit257.mandalore.uweather.api

import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.processphoenix.ProcessPhoenix

private const val KEY = "selected_city"

private fun getPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("uWeather", Context.MODE_PRIVATE)
}

fun getSelectedCity(context: Context): String? {
    return getPreferences(context).getString(KEY, "Gothenburg")
}

fun setSelectedCity(context: Context, city: String) {
    getPreferences(context).edit().putString(KEY, city).apply()
    ProcessPhoenix.triggerRebirth(context)
}