package dit257.mandalore.uweather

import android.net.http.HttpResponseCache
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import dit257.mandalore.uweather.databinding.ContentMainBinding
import dit257.mandalore.uweather.manager.CitiesManager
import dit257.mandalore.uweather.manager.PreferencesManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
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

        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)

        val cities = CitiesManager.getCities()

        binding.searchBox.setAdapter(
            ArrayAdapter(
                this, android.R.layout.simple_dropdown_item_1line,
                cities.toList()
            )
        )
        binding.searchBox.threshold = 1
        binding.searchBox.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                PreferencesManager.setSelectedCity(
                    this@MainActivity,
                    parent?.getItemAtPosition(position).toString()
                )
            }
        binding.searchBox.setOnKeyListener { _: View?, keyCode: Int, _: KeyEvent? ->
            if (KeyEvent.KEYCODE_ENTER == keyCode) {
                if (binding.searchBox.text.isNotEmpty() && !binding.searchBox.adapter.isEmpty)
                    PreferencesManager.setSelectedCity(
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
/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }


 */
/*
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
*/


    override fun onStop() {
        super.onStop()

        // Flush cache to file system so that it persists
        HttpResponseCache.getInstalled()?.flush()
    }
}