package dit257.mandalore.uweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.databinding.FragmentClimateBinding

// This is the fragment for the climate page. The only logic in here is for the different buttons and where they will take you.
class ClimateFragment : Fragment() {

    private var _binding: FragmentClimateBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentClimateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Switching from fragment "ClimateFragment" to "ClimateChangeFragment"
        binding.climateInfoButton.setOnClickListener {
            val nextFragment = ClimateChangeFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, nextFragment)?.commit()
        }
        //Switching from fragment "ClimateFragment" to "ClimateCauseFragment"
        binding.causeInfoButton.setOnClickListener {
            val nextFragment = ClimateCauseFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, nextFragment)?.commit()
            //Switching from fragment "ClimateFragment" to "ClimateActionFragment"
        }
        binding.actionInfoButton.setOnClickListener {
            val nextFragment = ClimateActionFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, nextFragment)?.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}