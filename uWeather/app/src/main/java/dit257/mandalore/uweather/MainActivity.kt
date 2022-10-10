package dit257.mandalore.uweather

import android.net.http.HttpResponseCache
import android.os.Bundle
import android.text.TextUtils.replace
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.*
import dit257.mandalore.uweather.databinding.ActivityMainBinding
import dit257.mandalore.uweather.databinding.ContentMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ContentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ContentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(FirstFragment())

       binding.bottomNavigationView.setOnItemSelectedListener {
           when(it.itemId){
               R.id.weather -> replaceFragment(FirstFragment())
               R.id.climate -> replaceFragment(SecondFragment())
               else ->{
               }
           }
           true
       }


        setSupportActionBar(binding.toolbar)

        //val navController = findNavController(R.id.nav_host_fragment_content_main)
        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        val user = arrayOf("Alingsås","Arboga","Arvika","Askersund","Avesta","Boden","Bollnäs","Borgholm","Borlänge","Borås","Djursholm","Eksjö","Enköping","Eskilstuna","Eslöv","Fagersta","Falkenberg","Falköping","Falsterbo","Falun","Filipstad","Flen","Gothenburg","Gränna","Gävle","Hagfors","Halmstad","Haparanda","Hedemora","Helsingborg","Hjo","Hudiksvall","Huskvarna","Härnösand","Hässleholm","Höganäs","Jönköping","Kalmar","Karlshamn","Karlskoga","Karlskrona","Karlstad","Katrineholm","Kiruna","Kramfors","Kristianstad","Kristinehamn","Kumla","Kungsbacka","Kungälv","Köping","Lahol","Landskrona","Lidingö","Lidköping","Lindesberg","Linköping","Ljungby","Ludvika","Luleå","Lund","Lycksele","Lysekil","Malmö","Mariefred","Mariestad","Marstrand","Mjölby","Motala","Nacka","Nora","Norrköping","Norrtälje","Nybro","Nyköping","Nynäshamn","Nässjö","Oskarshamn","Oxelösund","Piteå","Ronneby","Sala","Sandviken","Sigtuna","Simrishamn","Skanör","Skanör med Falsterbo","Skara","Skellefteå","Skänninge","Skövde","Sollefteå","Solna","Stockholm","Strängnäs","Strömstad","Sundbyberg","Sundsvall","Säffle","Säter","Sävsjö","Söderhamn","Söderköping","Södertälje","Sölvesborg","Tidaholm","Torshälla","Tranås","Trelleborg","Trollhättan","Trosa","Uddevalla","Ulricehamn","Umeå","Uppsala","Vadstena","Varberg","Vaxholm","Vetlanda","Vimmerby","Visby","Vänersborg","Värnamo","Västervik","Västerås","Växjö","Ystad","Åmål","Ängelholm","Örebro","Öregrund","Örnsköldsvik","Östersund","Östhammar")

        val userAdapter : ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            user
        )

        binding.userList.adapter = userAdapter;

        binding.searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (user.contains(query)){

                    userAdapter.filter.filter(query)

                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userAdapter.filter.filter(newText)
                return false
            }


        })
    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
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