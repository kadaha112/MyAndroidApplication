package activities

import MyApplication
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.daehankang.myandroidapplication.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore


class SignUpActivity : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signUpToolbar.setNavigationOnClickListener { finish() }

        binding.btnSignup.setOnClickListener { clickSignup() }

    }
    private fun clickSignup(){
        val email = binding.signUpEmail.editText!!.text.toString()
        val password = binding.signUpPassword.editText!!.text.toString()
        val confirmPassword = binding.signUpConfirmPassword.editText!!.text.toString()
        MyApplication.auth.createUserWithEmailAndPassword(email,password)

        if (password != confirmPassword){
            AlertDialog.Builder(this).setMessage("패스워드가 일치하지 않습니다. \n다시 입력해주세요.").create().show()
            binding.signUpConfirmPassword.editText!!.selectAll()
            return
        }

        val userRef : CollectionReference = Firebase.firestore.collection("emailUsers")

        userRef.whereEqualTo("email",email).get().addOnSuccessListener {
            if (it.documents.size>0){
                AlertDialog.Builder(this).setMessage("중복된 이메일이 있습니다. \n다시 입력해주세요.").create().show()
                binding.signUpEmail.editText!!.requestFocus()
                binding.signUpEmail.editText!!.selectAll()
            }else{
                val user : MutableMap<String,String> = mutableMapOf()
                user["email"] = email
                user["password"] = password

                userRef.document().set(user).addOnSuccessListener {
                    AlertDialog.Builder(this).setMessage("축하합니다 \n회원가입이 완료되었습니다")
                        .setPositiveButton("확인",{ p0, p1 -> finish()})
                        .create().show()
                }
            }
        }





    }
}