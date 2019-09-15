package take.dic.sensorapp.fragment.value

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.R
import take.dic.sensorapp.fragment.value.motion.AccelerationFragment
import take.dic.sensorapp.fragment.value.motion.GyroFragment
import take.dic.sensorapp.value.motion.MotionValue
import take.dic.sensorapp.fragment.value.motion.DirectionFragment

class ValueFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val motionValue = MotionValue(accelerationTitle = "加速度", gyroTitle =  "角速度", orientationTitle =  "方位")
        val bundle = Bundle()
        bundle.putSerializable("motionValue", motionValue)

        val motionValueArray = arrayOf<Fragment>(
            AccelerationFragment(),
            DirectionFragment(),
            GyroFragment()
        )
        motionValueArray.forEach { it.arguments = bundle }

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.add(R.id.gps_container, GPSFragment())
        transaction.add(R.id.acceleration_container, motionValueArray[0])
        transaction.add(R.id.orientation_container, motionValueArray[1])
        transaction.add(R.id.gyro_container, motionValueArray[2])
        transaction.add(R.id.beacon_container, BeaconFragment())
        transaction.commit()

        return inflater.inflate(R.layout.fragment_value, container, false)
    }
}