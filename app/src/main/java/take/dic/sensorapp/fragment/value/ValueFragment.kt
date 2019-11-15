package take.dic.sensorapp.fragment.value

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import kotlinx.coroutines.*
import retrofit2.Response
import take.dic.sensorapp.R
import take.dic.sensorapp.api.controller.ApiController
import take.dic.sensorapp.api.model.regular.RegularResponse
import take.dic.sensorapp.api.model.regular.image.ArrowImg
import take.dic.sensorapp.api.model.regular.image.AvatarImg
import take.dic.sensorapp.api.model.regular.image.BaseImg
import take.dic.sensorapp.fragment.image.ImageFragment
import take.dic.sensorapp.fragment.image.MyImage
import take.dic.sensorapp.fragment.value.motion.AccelerationFragment
import take.dic.sensorapp.fragment.value.motion.DirectionFragment
import take.dic.sensorapp.fragment.value.motion.GyroFragment
import take.dic.sensorapp.sensorvalue.GPSValue
import take.dic.sensorapp.sensorvalue.beacon.BeaconModel
import take.dic.sensorapp.sensorvalue.motion.MotionValue
import take.dic.sensorapp.sensorvalue.motion.motions.AccelerationValue
import take.dic.sensorapp.sensorvalue.motion.motions.DirectionValue
import take.dic.sensorapp.sensorvalue.motion.motions.GyroValue

class ValueFragment : Fragment() {
    private lateinit var initializeImage: Job
    private var hasSent = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val motionValue = MotionValue()
        val mBundle = Bundle()
        mBundle.putSerializable("motionValue", motionValue)

        val motionValueMap = mapOf<Int, Fragment>(
            R.id.acceleration_container to AccelerationFragment(),
            R.id.direction_container to DirectionFragment(),
            R.id.gyro_container to GyroFragment()
        )

        motionValueMap.forEach { it.value.arguments = mBundle }

        activity!!.supportFragmentManager.beginTransaction().apply {
            this.add(R.id.gps_container, GPSFragment())
            this.add(R.id.beacon_container, BeaconFragment())
            motionValueMap.forEach { this.add(it.key, it.value) }
        }.commit()

        return inflater.inflate(R.layout.fragment_value, container, false)

    }

    override fun onResume() {
        super.onResume()
        initializeImage = GlobalScope.launch(Dispatchers.Default) {
            while(!hasSent) {
                delay(2000)
                ApiController.sendSomeInformation(::setImage)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if(initializeImage.isActive) {
            initializeImage.cancel()
        }
        hasSent = false

        ApiController.sendAllInformation(System.currentTimeMillis()) {
            Log.d("allSend", it.toString())
        }

        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                realm.delete(GPSValue::class.java)
                realm.delete(BeaconModel::class.java)
                realm.delete(AccelerationValue::class.java)
                realm.delete(GyroValue::class.java)
                realm.delete(DirectionValue::class.java)
                realm.delete(MotionValue::class.java)
            }
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

    private fun setImage(response: Response<RegularResponse>) {
        val iBundle = Bundle()
        iBundle.putSerializable("initialImage", getImage(response))
        val imageFragment = ImageFragment()
        imageFragment.arguments = iBundle
        activity!!.supportFragmentManager.beginTransaction()
            .add(R.id.container, imageFragment).commit()
        hasSent = true
    }
}