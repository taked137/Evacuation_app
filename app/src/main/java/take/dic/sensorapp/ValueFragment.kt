package take.dic.sensorapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.acceleration.AccelerationValue
import take.dic.sensorapp.angular.AngularValue
import take.dic.sensorapp.beacon.BeaconValue
import take.dic.sensorapp.databinding.FragmentValueBinding
import take.dic.sensorapp.gps.GPSValue
import take.dic.sensorapp.orientation.OrientationValue

class ValueFragment : Fragment(){
    private val gps = GPSValue(title = "GPS", latitude = "緯度", longitude = "経度")
    private val acceleration = AccelerationValue(title = "加速度", x = "x軸", y = "y軸", z = "z軸")
    private val angular = AngularValue(title = "角速度", x = "x軸", y = "y軸", z = "z軸")
    private val orientation = OrientationValue(title = "方位", x = "x軸", y = "y軸", z = "z軸")
    private val beacon = BeaconValue()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentValueBinding.inflate(inflater, container, false)
        binding.gps = gps
        binding.acceleration = acceleration
        binding.angular = angular
        binding.orientation = orientation
        binding.beacon = beacon

        beacon.list.add("ここに")
        beacon.list.add("取得した値を")
        beacon.list.add("入れていきます")

        return binding.root
    }
}