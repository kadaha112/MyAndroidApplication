package activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.daehankang.myandroidapplication.databinding.ActivityMainCultureBinding

class MainActivityCulture : AppCompatActivity() {

    private val binding by lazy { ActivityMainCultureBinding.inflate(layoutInflater)  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginToolbar.setNavigationOnClickListener { finish() }

    }
}