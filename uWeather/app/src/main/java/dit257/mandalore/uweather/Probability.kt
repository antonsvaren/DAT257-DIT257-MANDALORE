package dit257.mandalore.uweather

import java.lang.Math.*
import java.util.*
import kotlin.math.abs

class Probability {

    //Calculates the mean from all data points provided by the API for a specific City. For now, hardcoded for Gothenburg
    fun calcProbabilityMean(allLastTemps:DoubleArray, temp:Double): Double {
        var total = 0.0
        for(t in allLastTemps){
            total += t
        }
        val mean = total / allLastTemps.size
        return 1 - (abs(temp - mean) / 100)
    }
    // We take all the recent temperatures for a specific city and calculating a interval of what the
    // temperature will be. With a 90% confidence level. z_alpha is decided on what the degrees of freedom((how many data points we have)-1 )
    // we choose and what level of confidence we are choosing.
    fun confidenceInterval(allLastTemps: DoubleArray): String{
        var variance = 0.0;
        var total = 0.0;
        for(t in allLastTemps){
            total += t
        }
        var mean = total/allLastTemps.size
        for(t in allLastTemps){
            variance += (pow((t - mean),2.0))/(allLastTemps.size-1);
        }
        var std = sqrt(variance);
        val z_alpha = 2.92
        var confidenceConstant = z_alpha*(std/sqrt(allLastTemps.size.toDouble()));
        var lower = round(mean-confidenceConstant);
        var upper = round(mean+confidenceConstant);
        return "Temperature will vary between ($lower°C-$upper°C)"
    }
}