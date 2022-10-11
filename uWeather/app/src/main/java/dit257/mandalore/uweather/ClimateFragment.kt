package dit257.mandalore.uweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.databinding.FragmentClimateBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ClimateFragment : Fragment() {

    private var _binding: FragmentClimateBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentClimateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.climateInfoButton.setOnClickListener{
            //TODO navigate to fragment about climate change
        }
        binding.causeInfoButton.setOnClickListener{
            //TODO navigate to fragment about climate change
        }
        binding.actionInfoButton.setOnClickListener{
            //TODO navigate to fragment about climate change
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}