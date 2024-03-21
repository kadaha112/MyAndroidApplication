package activities

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.data.Place
import com.daehankang.myandroidapplication.databinding.ActivityPlaceDetailBinding
import com.google.gson.Gson

class PlaceDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPlaceDetailBinding.inflate(layoutInflater) }

    private var isFavorite = false
    private lateinit var db : SQLiteDatabase
    private lateinit var place : Place
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val s : String?= intent.getStringExtra("place")
        s?.also{
            place = Gson().fromJson(it,Place::class.java)

            binding.wv.webViewClient = WebViewClient()
            binding.wv.webChromeClient = WebChromeClient()

            binding.wv.settings.javaScriptEnabled = true

            binding.wv.loadUrl(place.place_url)

        }

        db = openOrCreateDatabase("place", MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS favor(id TEXT PRIMARY KEY, place_name TEXT,category_name TEXT, phone TEXT, address_name TEXT, road_address_name TEXT, x TEXT,y TEXT,place_url TEXT, distance TEXT)")

        isFavorite = checkFavorite()

        if (isFavorite) binding.fabFavor.setImageResource(R.drawable.baseline_favorite_24)
        else binding.fabFavor.setImageResource(R.drawable.baseline_favorite_border_24)

        binding.fabFavor.setOnClickListener {
            if (isFavorite){
                // 찜 DB의 데이터를 삭제
                place.apply {
                    db.execSQL("DELETE FROM favor WHERE id = ?", arrayOf(id))
                }

                Toast.makeText(this, "찜 목록에서 제거되었습니다.", Toast.LENGTH_SHORT).show()
            }else{
                // 찜 DB에 데이터를 저장
                place.apply {
                    db.execSQL("INSERT INTO favor VALUES('$id','$place_name','$category_name','$phone','$address_name','$road_address_name','$x','$y','$place_url','$distance')")
                }
                Toast.makeText(this, "찜 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }

            isFavorite= !isFavorite
            if (isFavorite) binding.fabFavor.setImageResource(R.drawable.baseline_favorite_24)
            else binding.fabFavor.setImageResource(R.drawable.baseline_favorite_border_24)
        }

    }
    // SQLite Database의 찜 목록에 저장된 장소정보인지 체크하여 결과 여부를 리턴 [ true/ false ]
    private fun checkFavorite() : Boolean{

        // SQLite DB 의 "favor" 테이블에 현재장소에 대한 데이터가 있는지 확인
        place.apply {
            val cursor:Cursor = db.rawQuery("SELECT * FROM favor WHERE id = ?", arrayOf(id))
            // cursor는 검색조건에 해당하는 데이터를 가져와 만든 가상의 결과표 객체임
            // cursor.count : 총 레코드의 수
            if (cursor.count>0) return true
        }
        return false
    }

    // 뒤로가기 버튼처리
    override fun onBackPressed() {
        if (binding.wv.canGoBack()) binding.wv.goBack()
        else super.onBackPressed()
    }
}