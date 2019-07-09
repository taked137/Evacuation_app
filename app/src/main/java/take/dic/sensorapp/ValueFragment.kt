package take.dic.sensorapp

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
import take.dic.sensorapp.databinding.FragmentValueBinding
import take.dic.sensorapp.acceleration.AccelerationValue
import take.dic.sensorapp.angular.AngularValue
import take.dic.sensorapp.beacon.BeaconModel
import take.dic.sensorapp.beacon.BeaconValue
import take.dic.sensorapp.gps.GPSValue
import take.dic.sensorapp.orientation.OrientationValue
import java.text.SimpleDateFormat
import java.util.*

class ValueFragment : Fragment(), BeaconConsumer {

    private val gps = GPSValue(title = "GPS", latitude = "緯度", longitude = "経度")
    private val acceleration = AccelerationValue(title = "加速度", x = "x軸", y = "y軸", z = "z軸")
    private val angular = AngularValue(title = "角速度", x = "x軸", y = "y軸", z = "z軸")
    private val orientation = OrientationValue(title = "方位", x = "x軸", y = "y軸", z = "z軸")
    private val mBeacon = BeaconValue()
    private lateinit var state: String
    private lateinit var mRegion: Region
    private lateinit var realm: Realm

    //iBeacon認識のためのフォーマット設定
    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    private lateinit var beaconManager: BeaconManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentValueBinding.inflate(inflater, container, false)
        binding.gps = gps
        binding.acceleration = acceleration
        binding.angular = angular
        binding.orientation = orientation
        binding.beacon = mBeacon

        beaconManager = BeaconManager.getInstanceForApplication(activity!!)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
        binding.beacon = mBeacon

        Realm.init(activity!!)
        return binding.root
    }

    override fun getApplicationContext(): Context = activity!!.applicationContext

    override fun unbindService(p0: ServiceConnection?) {
        activity!!.unbindService(p0)
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean = activity!!.bindService(p0, p1, p2)

    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }

    //TODO: 試験用(削除予定)
    override fun onStop() {
        realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(BeaconModel::class.java)
        realm.commitTransaction()
        realm.close()

        super.onStop()
    }

    override fun onBeaconServiceConnect() {
        mRegion= Region("iBeacon", null, null, null)
        beaconManager.addMonitorNotifier(object : MonitorNotifier {
            override fun didEnterRegion(region: Region) {
                //領域への入場を検知
                state = "Enter Region"
            }

            override fun didExitRegion(region: Region) {
                //領域からの退場を検知
                state = "Exit Region"
            }

            override fun didDetermineStateForRegion(i: Int, region: Region) {
                //領域への入退場のステータス変化を検知
                state = "Determine State$i"
            }
        })
        try {
            //Beacon情報の監視を開始
            beaconManager.startMonitoringBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        beaconManager.addRangeNotifier { beacons, _ ->
            for (beacon in beacons) {
                realm = Realm.getDefaultInstance()
                var id: Int = try {
                    realm.where(BeaconModel::class.java).findAll().size + 1
                } catch (e: Exception) {
                    1
                }
                val currentBeacon = BeaconModel(
                    id,
                    beacon.id2.toString(),
                    beacon.id3.toString(),
                    beacon.rssi,
                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(Calendar.getInstance().time)
                )
                realm.executeTransaction {
                    val model = realm.createObject(BeaconModel::class.java!!, id)
                    model.major = currentBeacon.major
                    model.minor = currentBeacon.minor
                    model.rssi = currentBeacon.rssi
                    model.receivedTime = currentBeacon.receivedTime
                    realm.copyToRealm(model)
                }
                realm.where(BeaconModel::class.java).findAll().forEach { Log.e("got", it.toString()) }


                mBeacon.list.add(
                    0,
                    "${currentBeacon.receivedTime}\nmajor: ${currentBeacon.major}, minor: ${currentBeacon.minor}, rssi: ${currentBeacon.rssi}"
                )
                realm.close()
            }
        }
        try {
            beaconManager.startRangingBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
}