package dit257.mandalore.uweather

import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * We take all the recent temperatures for a specific city and calculating a interval of what the
 * temperature will be. With a 90% confidence level. z_alpha is decided on what the degrees of
 * freedom ((how many data points we have) - 1) we choose and what level of confidence we are
 * choosing.
 */
fun confidenceInterval(allLastTemps: Sequence<Double>): String {
    val size = allLastTemps.count()
    val mean = allLastTemps.average()

    var lower = mean.roundToInt()
    var upper = lower
    if (size > 1) {
        val variance = allLastTemps.map { (it - mean).pow(2) }.average()
        val zAlpha = arrayOf(6.314, 2.92)[size - 2]
        val confidenceConstant = zAlpha * sqrt(variance / size)
        lower = (mean - confidenceConstant).roundToInt()
        upper = (mean + confidenceConstant).roundToInt()
    }

    return if (lower == upper) "$lower°" else "$lower-$upper°"
}
