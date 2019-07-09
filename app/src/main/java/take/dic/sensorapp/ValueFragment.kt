package take.dic.sensorapp

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
import org.altbeacon.beacon.*
import take.dic.sensorapp.databinding.FragmentValueBinding
import java.text.SimpleDateFormat
import java.util.*

class ValueFragment : Fragment(), BeaconConsumer {
    private lateinit var state: String
    private lateinit var mRegion: Region
    private val beaconList = mutableListOf<BeaconModel>()
    private val data = mutableListOf<String>()

    //iBeacon認識のためのフォーマット設定
    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    private lateinit var beaconManager: BeaconManager

    override fun getApplicationContext(): Context = activity!!.applicationContext

    override fun unbindService(p0: ServiceConnection?) {
        //beaconManager.unbind(this)
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

    override fun onBeaconServiceConnect() {
        mRegion= Region("iBeacon", null, null, null)
        Log.e("hhh","epe")
        beaconManager.addMonitorNotifier(object : MonitorNotifier {
            override fun didEnterRegion(region: Region) {
                //領域への入場を検知
                state = "Enter Region"
                Log.e("hsh","$state")
            }

            override fun didExitRegion(region: Region) {
                //領域からの退場を検知
                state = "Exit Region"
                Log.e("hsh","$state")
            }

            override fun didDetermineStateForRegion(i: Int, region: Region) {
                //領域への入退場のステータス変化を検知
                state = "Determine State$i"
                Log.e("hsh","$state")
            }
        })
        try {
            //Beacon情報の監視を開始
            beaconManager.startMonitoringBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

        beaconManager.addRangeNotifier { beacons, _ ->
            //検出したBeaconの情報を全て出力
            for (beacon in beacons) {
                Log.e("fff",beacon.toString())
                //realm = Realm.getDefaultInstance()
                var id: Int = try {
                    //realm.where(BeaconModel::class.java).findAll().size + 1
                    1
                } catch (e: Exception) {
                    1
                }
                val currentBeacon = BeaconModel(
                    id,
                    beacon.id1.toString(),
                    beacon.id2.toString(),
                    beacon.rssi,
                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(Calendar.getInstance().time)
                )
                /*
                realm.executeTransaction {
                    val model = realm.createObject(BeaconModel::class.java!!, id)
                    model.major = currentBeacon.major
                    model.minor = currentBeacon.minor
                    model.rssi = currentBeacon.rssi
                    model.receivedTime = currentBeacon.receivedTime
                    realm.copyToRealm(model)
                }
                realm.where(BeaconModel::class.java).findAll().forEach { Log.e("got", it.toString()) }
                */

                data.add(
                    0,
                    "${currentBeacon.receivedTime}\nmajor: ${currentBeacon.major}, minor: ${currentBeacon.minor}, rssi: ${currentBeacon.rssi}"
                )
                //realm.close()
            }
            Log.e("hhh", data.toString())
        }
        try {
            beaconManager.startRangingBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }

    val gps = GPSValue(title = "GPS", latitude = "緯度", longitude = "経度")
    val beacon = BeaconValue()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentValueBinding.inflate(inflater, container, false)
        binding.gps = gps

        regist()
        binding.beacon = beacon
        return binding.root
    }

    private fun regist() {
        beaconManager = BeaconManager.getInstanceForApplication(activity!!)
        try{
            beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
        }catch(e: Exception){
            Log.e("eeee",e.toString())
        }
        beaconManager.bind(this)
    }
}