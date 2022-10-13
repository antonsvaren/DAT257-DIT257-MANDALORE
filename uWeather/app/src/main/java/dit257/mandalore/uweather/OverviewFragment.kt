package dit257.mandalore.uweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.api.PreferencesManager
import dit257.mandalore.uweather.api.WeatherService
import dit257.mandalore.uweather.databinding.FragmentOverviewBinding

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

        WeatherService.services.forEach {
            val temperature = it.getCurrentTemperature()
            when (it.name) {
                "SMHI" -> {
                    binding.smhitemp.text = "$temperature"
                }
                "Yr" -> {
                    binding.yrtemp.text = "$temperature"
                }
                "Mock" -> {
                    binding.mocktemp.text = "$temperature"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}