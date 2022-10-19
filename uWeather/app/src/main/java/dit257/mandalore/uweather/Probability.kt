package dit257.mandalore.uweather

import org.apache.commons.math3.distribution.TDistribution
import org.apache.commons.math3.stat.descriptive.SummaryStatistics
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * We take all the recent temperatures for a specific city and calculating a interval of what the
 * temperature will be. With a 90% confidence level. z_alpha is decided on what the degrees of
 * freedom ((how many data points we have) - 1) we choose and what level of confidence we are
 * choosing.
 */
fun confidenceInterval(allLastTemps: Sequence<Double>): String {
    val stats = SummaryStatistics().also { allLastTemps.forEach(it::addValue) }
    if (stats.n < 1) return "NaN"
    var lower = stats.mean.roundToInt()
    var upper = lower
    if (stats.n > 1) {
        val zAlpha = TDistribution(null, stats.n - 1.0).inverseCumulativeProbability(0.95)
        val confidenceConstant = zAlpha * sqrt(stats.variance / stats.n)
        lower = (stats.mean - confidenceConstant).roundToInt()
        upper = (stats.mean + confidenceConstant).roundToInt()
    }

    return if (lower == upper) "$lower°" else "$lower-$upper°"
}
