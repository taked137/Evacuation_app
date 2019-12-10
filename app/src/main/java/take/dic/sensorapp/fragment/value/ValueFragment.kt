package take.dic.sensorapp.fragment.value

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.Realm
import take.dic.sensorapp.R
import take.dic.sensorapp.api.controller.ApiController
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

        val view = inflater.inflate(R.layout.fragment_value, container, false)
        view.findViewById<TextView>(R.id.text_all_send).setOnClickListener {
            ApiController.sendAllInformation(null) {
                Log.d("allSend", it.toString())
            }
        }

            return view
    }

    override fun onStop() {
        super.onStop()

        // TODO: 本番用はDeleteしない
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
}