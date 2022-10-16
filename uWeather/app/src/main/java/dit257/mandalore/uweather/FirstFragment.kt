package dit257.mandalore.uweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dit257.mandalore.uweather.api.WeatherService
import dit257.mandalore.uweather.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherString = WeatherService.services.map { service ->
            val name = service.name
            val temperature = service.getCurrentTemperature()
            "$name: $temperature"
        }.joinToString("\n")

        binding.textviewFirst.text = weatherString

        val p = Probability()
        val allServices = WeatherService.services.toList()
        val allCurrentTemps = doubleArrayOf(0.0, 0.0, 0.0)
        for(i in allServices.indices){
            allCurrentTemps[i] = allServices[i].getCurrentTemperature()!!
        }

        binding.textviewProb.text = WeatherService.services.map { service ->
            val name = service.name
            val temperature = service.getCurrentTemperature()
            val prob = Math.round(p.calcMean(allCurrentTemps, temperature!!)*100)
            "$name: $temperature Probability: $prob%"
        }.joinToString("\n")

        binding.textviewProb.text = (p.confidenceInterval(allCurrentTemps)).plus("\nwith an accuracy of 90%")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}