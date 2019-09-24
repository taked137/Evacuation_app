package take.dic.sensorapp.fragment.value

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import take.dic.sensorapp.R
import take.dic.sensorapp.fragment.value.motion.AccelerationFragment
import take.dic.sensorapp.fragment.value.motion.DirectionFragment
import take.dic.sensorapp.fragment.value.motion.GyroFragment
import take.dic.sensorapp.value.beacon.BeaconModel
import take.dic.sensorapp.value.motion.MotionValue
import take.dic.sensorapp.value.motion.motions.AccelerationValue
import take.dic.sensorapp.value.motion.motions.DirectionValue
import take.dic.sensorapp.value.motion.motions.GyroValue

class ValueFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val motionValue = MotionValue()
        val bundle = Bundle()
        bundle.putSerializable("motionValue", motionValue)

        val motionValueMap = mapOf<Int, Fragment>(
            R.id.acceleration_container to AccelerationFragment(),
            R.id.direction_container to DirectionFragment(),
            R.id.gyro_container to GyroFragment()
        )
        motionValueMap.forEach { it.value.arguments = bundle }

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.add(R.id.gps_container, GPSFragment())
        transaction.add(R.id.beacon_container, BeaconFragment())
        motionValueMap.forEach { transaction.add(it.key, it.value) }
        transaction.commit()

        return inflater.inflate(R.layout.fragment_value, container, false)
    }

    //TODO: 試験用(削除予定)
    override fun onStop() {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                realm.delete(BeaconModel::class.java)
                realm.delete(AccelerationValue::class.java)
                realm.delete(GyroValue::class.java)
                realm.delete(DirectionValue::class.java)
                realm.delete(MotionValue::class.java)
            }
        }
        super.onStop()
    }
}