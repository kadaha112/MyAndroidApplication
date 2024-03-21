package activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.daehankang.myandroidapplication.G
import com.daehankang.myandroidapplication.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import data.UserAccount

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.loginToolbar.setNavigationOnClickListener { finish() }
        binding.signUpLoginBtn.setOnClickListener { clickLogin() }

    }

        private fun clickLogin(){

        var email = binding.inputLayoutEmail.editText!!.text.toString()
        var password = binding.inputLayoutPassword.editText!!.text.toString()

        val userRef : CollectionReference = Firebase.firestore.collection("emailUsers")
        userRef.whereEqualTo("email",email)
            .whereEqualTo("password",password)
            .get().addOnSuccessListener {
                if (it.documents.size > 0){

                        val id : String = it.documents[0].id
                         G.userAccount= UserAccount(id, email)

                        val intent = Intent(this, MainActivity::class.java)

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    }else{
                        AlertDialog.Builder(this).setMessage("이메일과 비밀번호를 다시 확인해주세요").create().show()
                        binding.inputLayoutEmail.editText!!.requestFocus()
                        binding.inputLayoutEmail.editText!!.selectAll()
                    }
                }
                }

    }
