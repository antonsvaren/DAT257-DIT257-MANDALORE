package dit257.mandalore.uweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dit257.mandalore.uweather.api.WeatherService
import dit257.mandalore.uweather.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val p = Probability()
        val allServices = WeatherService.services.toList()
        val allCurrentTemps = doubleArrayOf(0.0, 0.0, 0.0)
        for(i in allServices.indices){
            allCurrentTemps[i] = allServices[i].getCurrentTemperature()!!
        }

        binding.textviewSecond.text = WeatherService.services.map { service ->
            val name = service.name
            val temperature = service.getCurrentTemperature()
            val prob = Math.round(p.calcProbabilityMean(allCurrentTemps, temperature!!)*100)
            "$name: $temperature Probability: $prob%"
        }.joinToString("\n")

        binding.textviewThird.text = (p.confidenceInterval(allCurrentTemps)).plus("\nwith an accuracy of 90%")

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}