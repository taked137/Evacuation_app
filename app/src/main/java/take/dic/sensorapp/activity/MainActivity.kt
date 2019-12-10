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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import take.dic.sensorapp.R
import take.dic.sensorapp.api.controller.ApiController
import take.dic.sensorapp.api.model.regular.RegularResponse
import take.dic.sensorapp.api.model.regular.image.ArrowImg
import take.dic.sensorapp.api.model.regular.image.AvatarImg
import take.dic.sensorapp.api.model.regular.image.BaseImg
import take.dic.sensorapp.fragment.image.ImageFragment
import take.dic.sensorapp.fragment.image.MyImage
import take.dic.sensorapp.fragment.value.ValueFragment
import take.dic.sensorapp.service.DeviceInformationManager

class MainActivity : AppCompatActivity() {
    private lateinit var initJob: Job
    private lateinit var valueFragment: ValueFragment
    private lateinit var imageFragment: Fragment
    private var isAdded = false

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (isAdded) {
                        removeFragment(imageFragment)
                        isAdded = false
                    }
                    showFragment(valueFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    if (!isAdded) {
                        isAdded = true
                        setFragment(ImageFragment())
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        Realm.init(this)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)

        this.windowManager.defaultDisplay.getSize(DeviceInformationManager.size)
        DeviceInformationManager.apply {
            this.id =
                Settings.Secure.getString(this@MainActivity.contentResolver, Settings.Secure.ANDROID_ID)
            this.size.x -= 400
        }

        if (savedInstanceState == null) {
            valueFragment = ValueFragment()
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            setFragment(valueFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::initJob.isInitialized && initJob.isActive) {
            initJob.cancel()
        }
    }

    private fun setFragment(fragment: Fragment) {
        if (fragment::class == ImageFragment::class) {
            setImageFragment(fragment)
        } else {
            supportFragmentManager.beginTransaction().apply {
                this.replace(R.id.container, fragment)
                this.addToBackStack(null)
                this.commit()
            }
        }
    }

    private fun setImageFragment(fragment: Fragment) {
        initJob = GlobalScope.launch {
            ApiController.sendInitInformation {
                setInitImage(it, fragment)
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            this.add(R.id.container, fragment)
            this.commit()
        }
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            this.remove(fragment)
            this.commit()
        }
    }

    private fun hideFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            this.hide(fragment)
            this.commit()
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            this.show(fragment)
            this.commit()
        }
    }

    private fun getImage(response: Response<RegularResponse>): MyImage {
        val body = response.body()!!
        return MyImage(
            AvatarImg(body.avatarImg.URL),
            BaseImg(body.baseImg.URL, body.baseImg.deg, body.baseImg.offset, body.baseImg.exp),
            ArrowImg(body.arrowImg.URL, body.arrowImg.deg)
        )
    }

    private fun setInitImage(response: Response<RegularResponse>, fragment: Fragment) {
        val iBundle = Bundle()
        iBundle.putSerializable("initialImage", getImage(response))
        fragment.arguments = iBundle

        imageFragment = fragment
        addFragment(fragment)

        hideFragment(valueFragment)
    }
}