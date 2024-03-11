package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.loginToolbar.setNavigationOnClickListener { finish() }
        binding.signUpLoginBtn.setOnClickListener { clickLogin() }

    }

    private fun clickLogin(){

    }
}