package activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daehankang.myandroidapplication.R
import com.daehankang.myandroidapplication.databinding.ActivityMainBinding
import fragments.FragmentMainAccount
import fragments.FragmentMainFavor
import fragments.FragmentMainHome
import fragments.FragmentMainMap

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.container_fragment,FragmentMainHome()).commit()

        binding.bnv.setOnItemSelectedListener {
            when (it.itemId){
                R.id.menu_btm_home -> supportFragmentManager.beginTransaction().replace(R.id.container_fragment,FragmentMainHome()).commit()
                R.id.menu_btm_map -> supportFragmentManager.beginTransaction().replace(R.id.container_fragment,FragmentMainMap()).commit()
                R.id.menu_btm_favor -> supportFragmentManager.beginTransaction().replace(R.id.container_fragment,FragmentMainFavor()).commit()
                R.id.menu_btm_account -> supportFragmentManager.beginTransaction().replace(R.id.container_fragment,FragmentMainAccount()).commit()
            }
            true
        }





    }
}