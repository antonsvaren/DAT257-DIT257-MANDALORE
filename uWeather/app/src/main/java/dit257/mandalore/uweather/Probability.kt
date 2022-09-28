package dit257.mandalore.uweather

import java.util.*

class Probability {

    //Calculate probability based on difference from median
    fun calcProbabilityMedian(allLastTemps:DoubleArray, temp:Double): Double {
        var total = 0.0
        var max = 0.0
        var min = 0.0
        for(t in allLastTemps){
            total += t
            if(t<min){min=t}
            if(t>max){max=t}
        }
        val median = total / allLastTemps.size
        return 1 - ((temp - median) / (max-min))
    }
    //Calculate probability based on providers track record
    fun calcProbabilityTrackRecord(provider:LinkedList<Int>, actual_temp:LinkedList<Int>):Double{
        var difference = 0.0
        var nrOfReadings = 0
        var max = 0
        var min = 0
        for (i in provider){
            //Get min/max temperature reported
            if(i<min){min=i}
            if(actual_temp.get(nrOfReadings-1)<min){min=i}
            if(i>max){max=i}
            if(actual_temp.get(nrOfReadings-1)>max){max=i}

            nrOfReadings++
            difference += i - actual_temp.get(nrOfReadings-1)
        }
        /* Probability is = 1 - all previous difference from actual temp / span of temperatures,
         i.e. if temperatures have ranged from 0°C - 10°C and a provider has differed by 5°C on
         average, probability will be 0.5 */
        return 1 - ((difference/nrOfReadings) / (max-min))
    }
}