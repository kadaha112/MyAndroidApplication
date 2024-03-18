package activities

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.data.KakaoSearchPlaceResponse
import com.daehankang.myandroidapplication.databinding.ActivityMainFoodBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivityFood : AppCompatActivity() {

    private val binding by lazy { ActivityMainFoodBinding.inflate(layoutInflater) }

    var searchQuery : String = "내주변맛집"

    var myLocation : Location?= null
    val locationProviderClient : FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    var searchPlaceResponse : KakaoSearchPlaceResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginToolbar.setNavigationOnClickListener { finish() }

    }
}