package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daehankang.myandroidapplication.databinding.ActivityStartSignUpBinding

class StartSignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivityStartSignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.signUp.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
        binding.browse.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

    }
}