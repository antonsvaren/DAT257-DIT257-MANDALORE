package dit257.mandalore.uweather

import java.lang.Math.*
import java.util.*
import kotlin.math.abs

class Probability {

    //Calculate probability based on difference from median
    fun calcProbabilityMean(allLastTemps:DoubleArray, temp:Double): Double {
        var total = 0.0
        for(t in allLastTemps){
            total += t
        }
        val mean = total / allLastTemps.size
        return 1 - (abs(temp - mean) / 100)
    }

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