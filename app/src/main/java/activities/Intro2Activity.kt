package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daehankang.myandroidapplication.databinding.ActivityIntro2Binding

class Intro2Activity : AppCompatActivity() {

    private val binding by lazy { ActivityIntro2Binding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

            binding.introStart.setOnClickListener {startActivity(Intent(this, StartSignUpActivity::class.java))}
            binding.introLogin.setOnClickListener {startActivity(Intent(this, LoginActivity::class.java))}



    }
}