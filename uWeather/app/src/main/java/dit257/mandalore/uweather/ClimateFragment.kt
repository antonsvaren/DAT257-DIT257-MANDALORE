package dit257.mandalore.uweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.databinding.FragmentClimateBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dit257.mandalore.uweather.api.ClimateActionFragment
import dit257.mandalore.uweather.api.ClimateCauseFragment

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
            val nextFragment = ClimateChangeFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, nextFragment)?.commit()
        }
        binding.causeInfoButton.setOnClickListener{
            val nextFragment = ClimateCauseFragment()
        }
        binding.actionInfoButton.setOnClickListener{
            val nextFragment = ClimateActionFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}