package dit257.mandalore.uweather

abstract class WeatherService {
    abstract fun getTemperatureNextHour(): Float
    abstract fun getTemeratures24h(): Array <Pair<Int,Float>>

}