package dit257.mandalore.uweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dit257.mandalore.uweather.databinding.ActivityScrollingBinding

class ScrollingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_scrolling)

    }
}