package dit257.mandalore.uweather

import android.content.Intent
import android.net.http.HttpResponseCache
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import dit257.mandalore.uweather.api.getSelectedCity
import dit257.mandalore.uweather.api.updateAll
import java.io.File


class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@LoadingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)

        // Create 10mb response cache
        HttpResponseCache.install(File(cacheDir, "http"), 10485760)

        updateAll(getSelectedCity(this) ?: return)
    }
}