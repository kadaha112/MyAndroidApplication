package fragments

import activities.MainActivity
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daehankang.myandroidapplication.data.KakaoSearchPlaceResponse
import com.daehankang.myandroidapplication.databinding.FragmentMainMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapView


class FragmentMainMap : Fragment() {

    private val binding by lazy { FragmentMainMapBinding.inflate(layoutInflater) }
    var searchQuery : String = ""
    var myLocation:Location?= null



    // kakao search API 응답결과 객체 참조변수
    var searchPlaceResponse: KakaoSearchPlaceResponse?= null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mapView = MapView(context)
        binding.homeMapView.addView(mapView)

        return binding.root
    }


}