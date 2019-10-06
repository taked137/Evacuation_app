package take.dic.sensorapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import take.dic.sensorapp.R
import take.dic.sensorapp.fragment.image.ImageFragment
import take.dic.sensorapp.fragment.value.ValueFragment
import take.dic.sensorapp.service.DeviceInformationManager


class MainActivity : AppCompatActivity() {

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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, ValueFragment())
                .commit()
        }

        // 画像を表示するfragmentを追加します(雑)
        supportFragmentManager.beginTransaction().add(R.id.container, ImageFragment()).commit()
    }
}