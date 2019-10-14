package take.dic.sensorapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import take.dic.sensorapp.R
import take.dic.sensorapp.fragment.image.ImageFragment
import take.dic.sensorapp.fragment.value.ValueFragment
import take.dic.sensorapp.service.DeviceInformationManager

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setFragment(ValueFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                setFragment(ImageFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Realm.init(this)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

        supportActionBar!!.hide()
        DeviceInformationManager.id =
            Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setFragment(ValueFragment())
    }

    private fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            this.replace(R.id.container, fragment)
            this.addToBackStack(null)
            this.commit()
        }
    }
}