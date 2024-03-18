package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.daehankang.myandroidapplication.databinding.ActivityStartSignUpBinding
import network.RetrofitHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import data.UserAccount
import com.daehankang.myandroidapplication.G
import network.RetrofitApiService
import retrofit2.Call
import retrofit2.Response

class StartSignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivityStartSignUpBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val key = Utility.getKeyHash(this)
        Log.i("key",key)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.signUp.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
        binding.browse.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
        binding.clickGoogle.setOnClickListener { clickGoogle() }
        binding.clickKakao.setOnClickListener { clickKakao() }
        binding.clickNaver.setOnClickListener { clickNaver() }
    }
    private fun clickGoogle(){
        // 로그인 옵션객체 생성 - Builder - 이메일 요청..
        val signInOptions: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        // 구글 로그인을 하는 화면 액티비티를 실행하는 Intent 객체로 로그인
        val intent: Intent = GoogleSignIn.getClient(this, signInOptions).signInIntent
        resultLauncher.launch(intent)
    }
    val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        // 로그인결과를 가져온 인텐트 소환
        val intent : Intent? = it.data
        // 인텐트로부터 구글계정정보를 가져오는 작업자 객체를 소환
        val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

        // 작업자로부터 계정 받기
        val account : GoogleSignInAccount = task.result
        val id : String = account.id.toString()
        val email: String = account.email ?: ""

        Toast.makeText(this, "$id\n$email", Toast.LENGTH_SHORT).show()
        G.userAccount = UserAccount(id, email)

        // main 화면으로 이동
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
    private fun clickKakao(){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(this, "카카오로그인 실패", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "카카오로그인 성공", Toast.LENGTH_SHORT).show()

                // 사용자 정보 요청
                UserApiClient.instance.me { user, error ->
                    if (user != null) {
                        val id: String = user.id.toString()
                        val nickname: String = user.kakaoAccount?.profile?.nickname ?: ""

                        Toast.makeText(this, "$id\n$nickname", Toast.LENGTH_SHORT).show()
                        G.userAccount = UserAccount(id,nickname)

                        // 로그인 되었으니
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }

        // 카카오톡이 사용가능하면 이를 이용하여 로그인하고 없으면 카카오계정으로 로그인하기
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback= callback)
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback= callback)
        }

    }
    private fun clickNaver(){
        NaverIdLoginSDK.initialize(this,"lHP60yGqKrkg9b7IWIiv", "FVfdJDSyKZ","Search Place")

        // 로그인 요청
        NaverIdLoginSDK.authenticate(this,object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                Toast.makeText(this@StartSignUpActivity, "$message", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Toast.makeText(this@StartSignUpActivity, "$message", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess() {
                Toast.makeText(this@StartSignUpActivity, "로그인에 성공했습니다", Toast.LENGTH_SHORT).show()

                // 사용자 정보를 받아오기.. -- REST API로 받아야 함.
                // 로그인에 성공하면. REST API로 요청할 수 있는 토큰(token)
                val accessToken:String? = NaverIdLoginSDK.getAccessToken()

                // Restofit 작업을 통해 사용자 정보 가져오기
                val retrofit = RetrofitHelper.getRetrofitInstance("https://openapi.naver.com")
                val retrofitApiService = retrofit.create(RetrofitApiService::class.java)
                val call = retrofitApiService.getNidUserInfo("Bearer ${accessToken}")
                call.enqueue(object : retrofit2.Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val s = response.body()
                        AlertDialog.Builder(this@StartSignUpActivity).setMessage(s).create().show()

                        startActivity(Intent(this@StartSignUpActivity, MainActivity::class.java))
                        finish()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(this@StartSignUpActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                    }

                })
            }

        })


    }
}