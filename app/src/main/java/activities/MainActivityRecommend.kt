package activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.databinding.ActivityMainRecommendBinding

class MainActivityRecommend : AppCompatActivity() {

    private val binding by lazy { ActivityMainRecommendBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginToolbar.setNavigationOnClickListener { finish() }



    }
}