package dit257.mandalore.uweather

import kotlin.math.*

/**
 * We take all the recent temperatures for a specific city and calculating a interval of what the
 * temperature will be. With a 90% confidence level. z_alpha is decided on what the degrees of
 * freedom ((how many data points we have) - 1) we choose and what level of confidence we are
 * choosing.
 */
fun confidenceInterval(allLastTemps: Sequence<Double>): String {
    val count = allLastTemps.count()
    val mean = allLastTemps.average()
    val variance = allLastTemps.sumOf { (it - mean).pow(2) } / (count - 1)
    val std = sqrt(variance)
    val zAlpha = 2.92
    val confidenceConstant = zAlpha * std / sqrt(count.toDouble())
    val lower = (mean - confidenceConstant).roundToInt()
    val upper = (mean + confidenceConstant).roundToInt()
    if(lower == upper){
        return "$lower°"
    }
    else return "$lower-$upper°"

}