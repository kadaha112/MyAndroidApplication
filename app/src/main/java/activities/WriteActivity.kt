package activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityWriteBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginToolbar.setNavigationOnClickListener { finish() }
        binding.btnSelectImg.setOnClickListener { clickSelect() }

    }
    private fun clickSelect(){
        val intent= if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES) else Intent(
            Intent.ACTION_OPEN_DOCUMENT).setType("image/*")

    }



}