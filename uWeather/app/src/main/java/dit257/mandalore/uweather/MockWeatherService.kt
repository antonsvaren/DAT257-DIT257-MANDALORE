package dit257.mandalore.uweather

import kotlin.random.Random


class MockWeatherService : WeatherService() {
    // generates a random temperature forecast between -20.0 and +30.0
    override fun getTemperatureNextHour(): Float{
        return  Random.nextInt(-200,+300).toFloat()/10
    }

    //just a mock implementation currently
    override fun getTemeratures24h(): Array <Pair<Int,Float>>{
        return arrayOf(Pair(1,1.1.toFloat()))
    }

}