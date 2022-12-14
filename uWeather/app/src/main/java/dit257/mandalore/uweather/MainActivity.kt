package dit257.mandalore.uweather

import android.net.http.HttpResponseCache
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dit257.mandalore.uweather.api.CITIES
import dit257.mandalore.uweather.api.setSelectedCity
import dit257.mandalore.uweather.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(OverviewFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.weather -> replaceFragment(OverviewFragment())
                R.id.climate -> replaceFragment(ClimateFragment())
                else -> {
                }
            }
            true
        }


        setSupportActionBar(binding.toolbar)


        binding.searchBox.setAdapter(
            ArrayAdapter(
                this, android.R.layout.simple_dropdown_item_1line,
                CITIES.keys.toList()
            )
        )
        binding.searchBox.threshold = 1
        binding.searchBox.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                setSelectedCity(
                    this@MainActivity,
                    parent?.getItemAtPosition(position).toString()
                )
            }
        binding.searchBox.setOnKeyListener { _: View?, keyCode: Int, _: KeyEvent? ->
            if (KeyEvent.KEYCODE_ENTER == keyCode) {
                if (binding.searchBox.text.isNotEmpty() && !binding.searchBox.adapter.isEmpty)
                    setSelectedCity(
                        this@MainActivity,
                        binding.searchBox.adapter.getItem(0).toString()
                    )
                return@setOnKeyListener true
            }
            false
        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


    override fun onStop() {
        super.onStop()

        // Flush cache to file system so that it persists
        HttpResponseCache.getInstalled()?.flush()
    }
}