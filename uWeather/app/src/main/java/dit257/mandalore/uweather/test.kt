package dit257.mandalore.uweather

import java.util.*

class test {
    val p = Probability()

    //Placeholder lists that contain all temperatures reported
    var provider1_reports = LinkedList<Int>()
    var provider2_reports = LinkedList<Int>()
    var provider3_reports = LinkedList<Int>()
    var provider4_reports = LinkedList<Int>()
    var actual_temp = LinkedList<Int>()

    fun addTempToLists(){
        provider1_reports.add(getRandomTemp())
        provider2_reports.add(getRandomTemp())
        provider3_reports.add(getRandomTemp())
        provider4_reports.add(getRandomTemp())
        actual_temp.add(getRandomTemp())
    }

    fun getRandomTemp(): Int{
        return (-20..40).random();
    }

    fun main(){
        val last_temps =
            intArrayOf(provider1_reports.last,provider2_reports.last,provider3_reports.last,provider4_reports.last)
        val probability_median = p.calcProbabilityMedian(last_temps)
        println("$last_temps")
        println("Probability from median is: $probability_median")
    }
}