package dit257.mandalore.uweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.api.WeatherService
import dit257.mandalore.uweather.databinding.FragmentOverviewBinding
import dit257.mandalore.uweather.manager.PreferencesManager
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.city.text = PreferencesManager.getSelectedCity(view.context)

        val format = DateTimeFormatter.ofPattern("ha")
        var time = WeatherService.getCurrentTime()
        binding.degrees.text = WeatherService.getAverageTemperature(time)
        for (i in 0 until 5) {
            time = time.plusHours(1)
            view.findViewById<TextView>(R.id.time1 + i).text = time.plusHours(2).format(format)
            view.findViewById<TextView>(R.id.future1 + i).text = WeatherService.getAverageTemperature(time)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}