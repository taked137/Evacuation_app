package take.dic.sensorapp.fragment.value

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationProvider
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import take.dic.sensorapp.databinding.FragmentGpsBinding
import take.dic.sensorapp.value.GPSValue

class GPSFragment : android.support.v4.app.Fragment(), LocationListener {

    private lateinit var locationManager: LocationManager
    private val gps = GPSValue(
        title = "GPS",
        latitude = "",
        longitude = "",
        altitude = ""
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < 23 || checkPermission()) {
            locationStart()

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                50f,
                this
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        val binding = FragmentGpsBinding.inflate(inflater, container, false)
        binding.gps = gps

        return binding.root
    }

    //使用可能かどうかの判定。trueなら可能、falseなら不可能、許可を申請する
    private fun checkPermission(): Boolean {
        val judge = (ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)

        return if (judge) {
            true
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
            false
        }
    }

    private fun locationStart() {

        Log.d("debug", "locationStart()")

        // LocationManager インスタンス生成
        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled")
        } else {
            // GPSを設定するように促す
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
            Log.d("debug", "not gpsEnable, startActivity")
        }


        if (!checkPermission()) {
            Log.d("debug", "checkSelfPermission false")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000, 50f, this
        )

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

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        when (status) {
            LocationProvider.AVAILABLE -> Log.d("debug", "LocationProvider.AVAILABLE")
            LocationProvider.OUT_OF_SERVICE -> Log.d("debug", "LocationProvider.OUT_OF_SERVICE")
            LocationProvider.TEMPORARILY_UNAVAILABLE -> Log.d(
                "debug",
                "LocationProvider.TEMPORARILY_UNAVAILABLE"
            )
        }
    }


    override fun onLocationChanged(location: Location) {
        gps.setResult(
            System.currentTimeMillis().toString(),
            location.latitude.toFloat(),
            location.longitude.toFloat(),
            location.altitude.toFloat()
        )

        if (checkPermission()) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000, 50f, this
            )
        }
        //位置情報更新

        //locationStart()
    }

    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}