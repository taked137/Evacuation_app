package take.dic.sensorapp.fragment.value

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import org.altbeacon.beacon.*
import take.dic.sensorapp.R
import take.dic.sensorapp.fragment.value.acceleration.AccelerationSensorFragment
import take.dic.sensorapp.fragment.value.beacon.BeaconModel
import take.dic.sensorapp.fragment.value.beacon.BeaconValue
import take.dic.sensorapp.databinding.FragmentValueBinding
import take.dic.sensorapp.fragment.value.beacon.BeaconFragment
import take.dic.sensorapp.fragment.value.gps.GPSFragment
import take.dic.sensorapp.fragment.value.gyro.GyroSensorFragment
import take.dic.sensorapp.fragment.value.orientation.OrientationFragment
import java.text.SimpleDateFormat
import java.util.*

class ValueFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentValueBinding.inflate(inflater, container, false)

        val transaction = activity!!.supportFragmentManager.beginTransaction()
        transaction.add(R.id.gps_container, GPSFragment())
        transaction.add(R.id.acceleration_container, AccelerationSensorFragment())
        transaction.add(R.id.orientation_container, OrientationFragment())
        transaction.add(R.id.gyro_container, GyroSensorFragment())
        transaction.add(R.id.beacon_container, BeaconFragment())
        transaction.commit()

        return binding.root
    }
}