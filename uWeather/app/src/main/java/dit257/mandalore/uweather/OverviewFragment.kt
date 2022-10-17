package dit257.mandalore.uweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.api.*
import dit257.mandalore.uweather.databinding.FragmentOverviewBinding
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import kotlin.math.roundToInt

class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.city.text = getSelectedCity(view.context)

        val uvIndex = SERVICES.map { it.uvIndex }.filterNotNull().average()
        binding.uvIndex.text = when {
            uvIndex <= 2 -> "Low"
            uvIndex <= 5 -> "Moderate"
            uvIndex <= 7 -> "High"
            uvIndex <= 10 -> "Very High"
            else -> "Extreme"
        }

        val tempFormat = "%.1f°"
        val timeFormat = DateTimeFormatter.ofPattern("HH:00")
        var time = getCurrentTime()
        binding.degrees.text = tempFormat.format(getTemperatures(time).average())
        for (i in 0 until 5) {
            time = time.plusHours(1)
            view.findViewById<TextView>(R.id.time1 + i).text = time.plusHours(2).format(timeFormat)
            view.findViewById<TextView>(R.id.degrees1 + i).text =
                (confidenceInterval(getTemperatures(time)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}