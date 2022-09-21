package dit257.mandalore.uweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@LoadingActivity, MainActivity::class.java)
            startActivity(intent)
            finish();
        }, 3000)
    }
}