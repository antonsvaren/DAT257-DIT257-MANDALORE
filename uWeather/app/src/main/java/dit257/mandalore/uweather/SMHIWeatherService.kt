package dit257.mandalore.uweather

class SMHIWeatherService : WeatherService() {
    // gets the Temperature gor the next hour from SMHI
    override fun getTemperatureNextHour(): Float {
        return API.current.getJSONArray("timeSeries").getJSONObject(0).getJSONArray("parameters").getJSONObject(10).getJSONArray("values").getString(0).toFloat()
    }

    //just a mock implementation currently
    override fun getTemeratures24h(): Array<Pair<Int, Float>> {
        return arrayOf(Pair(1, 1.1.toFloat()))
    }
}