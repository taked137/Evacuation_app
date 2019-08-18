package take.dic.sensorapp.fragment.value

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.R
import take.dic.sensorapp.fragment.value.acceleration.AccelerationSensorFragment
import take.dic.sensorapp.fragment.value.beacon.BeaconFragment
import take.dic.sensorapp.fragment.value.gps.GPSFragment
import take.dic.sensorapp.fragment.value.gyro.GyroSensorFragment
import take.dic.sensorapp.fragment.value.orientation.OrientationFragment

class ValueFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.add(R.id.gps_container, GPSFragment())
        transaction.add(R.id.acceleration_container, AccelerationSensorFragment())
        transaction.add(R.id.orientation_container, OrientationFragment())
        transaction.add(R.id.gyro_container, GyroSensorFragment())
        transaction.add(R.id.beacon_container, BeaconFragment())
        transaction.commit()

        return inflater.inflate(R.layout.fragment_value, container, false);
    }
}