package activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUpToolbar.setNavigationOnClickListener { finish() }

    }
}