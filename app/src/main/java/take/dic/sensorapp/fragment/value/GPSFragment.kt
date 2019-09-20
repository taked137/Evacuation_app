package take.dic.sensorapp.fragment.value

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.*
import take.dic.sensorapp.databinding.FragmentGpsBinding
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment
import take.dic.sensorapp.value.GPSValue

class GPSFragment : BaseBindingFragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val gps = GPSValue().apply {
        this.titleWord.set("GPS")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission()
        } else {
            locationStart()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentGpsBinding.inflate(inflater, container, false)
        binding.gps = gps

        return binding.root
    }

    //使用可能かどうかの判定。trueなら可能、falseなら不可能、許可を申請する
    private fun checkPermission() {
        val judge = (ContextCompat.checkSelfPermission(
            activity!!, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)

        if (judge) {
            locationStart()
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1000)
        }

    }

    private fun locationStart() {

        Log.d("debug", "locationStart()")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        val locationRequest = LocationRequest()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                // 更新直後の位置が格納されているはず
                val location = locationResult?.lastLocation ?: return
                gps.setResult(location)
            }
        }

        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == 1000) {
            // 使用が許可された
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug", "checkSelfPermission true")
                locationStart()

            } else {
                // それでも拒否された時の対応
                Toast.makeText(context!!, "GPS機能は作動しません", Toast.LENGTH_SHORT).show()
            }
        }
    }
}