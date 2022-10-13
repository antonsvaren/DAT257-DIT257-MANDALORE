package dit257.mandalore.uweather.manager

import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.processphoenix.ProcessPhoenix


class PreferencesManager {
    companion object {
        private const val selectedCityKey = "selected_city"

        private fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("uWeather", Context.MODE_PRIVATE)
        }

        fun getSelectedCity(context: Context): String? {
            return getPreferences(context).getString(selectedCityKey, "Gothenburg")
        }

        fun setSelectedCity(context: Context, city: String) {
            getPreferences(context).edit().putString(selectedCityKey, city).apply()
            ProcessPhoenix.triggerRebirth(context)
        }
    }
}