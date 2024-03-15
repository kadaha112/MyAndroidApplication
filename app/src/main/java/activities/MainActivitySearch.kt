package activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.databinding.ActivityMainSearchBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.daehankang.myandroidapplication.data.KakaoSearchPlaceResponse
import com.daehankang.myandroidapplication.data.Place
import com.daehankang.myandroidapplication.data.PlaceMeta
import com.daehankang.myandroidapplication.databinding.FragmentPlaceListBinding
import com.daehankang.tpsearchplacebykakao.network.RetrofitHelper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import fragments.PlaceListFragment
import network.RetrofitApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivitySearch : AppCompatActivity() {

    private val binding by lazy { ActivityMainSearchBinding.inflate(layoutInflater) }
    var searchQuery : String = "카페"
    var myLocation : Location? = null
    val locationProviderClient : FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    var searchPlaceResponse: KakaoSearchPlaceResponse?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginToolbar.setNavigationOnClickListener { finish() }

        supportFragmentManager.beginTransaction().add(R.id.container_fragment, PlaceListFragment()).commit()

        binding.etSearch.setOnEditorActionListener{ v, actionId, event ->
            searchQuery = binding.etSearch.text.toString()
            searchPlaces()
            false
            // 특정 키워드 단축 choice 버튼들에 리스너 처리하는 코드를 별도의 메소드에..

        }
        setChoiceButtonsListener()

        // 위치정보 제공에 대한 퍼미션 체크
        val permissionState: Int= checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if(permissionState==PackageManager.PERMISSION_DENIED){
            //퍼미션을 요청하는 다이얼로그 보이고 그 결과를 받아오는 작업을 대신해주는 대행사 이용
            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }else{
            //위치정보수집이 허가되어 있다면.. 곧바로 위치정보 얻어오는 작업 시작
            requestMyLocation()
        }

    }



    val permissionResultLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        if(it) requestMyLocation()
        else Toast.makeText(this, "내 위치정보를 제공하지 않아 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT).show()
    }
    private fun requestMyLocation(){

        //요청 객체 생성
        val request: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000).build()

        //실시간 위치정보 갱신 요청 - 퍼미션 체크코드가 있어야만 함.
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationProviderClient.requestLocationUpdates(request,locationCallback, Looper.getMainLooper())
    }
    private val locationCallback= object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            myLocation= p0.lastLocation

            //위치 탐색이 종료되었으니 내 위치 정보 업데이트를 이제 그만...
            locationProviderClient.removeLocationUpdates(this) //this: locationCallback 객체

            //위치정보를 얻었으니.. 키워드 장소검색 작업 시작!
            searchPlaces()
        }
    }

    private fun setChoiceButtonsListener(){
        binding.layoutChoice.choice01.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.choice02.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.choice03.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.choice04.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.choice05.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.choice06.setOnClickListener { clickChoice(it) }
        binding.layoutChoice.choice07.setOnClickListener { clickChoice(it) }

    }

    //멤버변수(property)
    var choiceID= R.id.choice01

    private fun clickChoice(view: View){

        //기존에 선택되었던 ImageView를 찾아서 배경이미지를 선택되지 않은 하얀색 원그림으로 변경
        findViewById<ImageView>(choiceID).setBackgroundResource(R.drawable.bg_choice)

        //현재 클릭한 ImageView의 배경을 선택된 회색 원그림으로 변경
        view.setBackgroundResource(R.drawable.bg_choice_selected)

        //클릭한 뷰의 id를 저장
        choiceID= view.id

        when(choiceID){
            R.id.choice01->searchQuery="카페"
            R.id.choice02->searchQuery="편의점"
            R.id.choice03->searchQuery="주차장"
            R.id.choice04->searchQuery="주유소"
            R.id.choice05->searchQuery="약국"
            R.id.choice06->searchQuery="꽃집"
            R.id.choice07->searchQuery="화장실"

        }

        //바뀐 검색장소명으로 검색 요청
        searchPlaces()

        //검색창에 글씨가 있다면 지우기..
        binding.etSearch.text.clear()
        binding.etSearch.clearFocus()
    }
    private fun searchPlaces(){
        Toast.makeText(this, "$searchQuery\n${myLocation?.latitude},${myLocation?.longitude}", Toast.LENGTH_SHORT).show()

        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitApiService = retrofit.create(RetrofitApiService::class.java)
        val call = retrofitApiService.searchPlace(searchQuery, myLocation?.longitude.toString(),myLocation?.latitude.toString())
        call.enqueue(object  : Callback<KakaoSearchPlaceResponse>{
            override fun onResponse(
                call: Call<KakaoSearchPlaceResponse>,
                response: Response<KakaoSearchPlaceResponse>
            ) {
                searchPlaceResponse = response.body()

                val meta : PlaceMeta? = searchPlaceResponse?.meta
                val documents : List<Place>? = searchPlaceResponse?.documents


            }

            override fun onFailure(call: Call<KakaoSearchPlaceResponse>, t: Throwable) {
                Toast.makeText(this@MainActivitySearch, "서버 오류가 있습니다", Toast.LENGTH_SHORT).show()
            }

        })



    }
}