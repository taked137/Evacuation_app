package take.dic.sensorapp.fragment.value

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import org.altbeacon.beacon.*
import take.dic.sensorapp.R
import take.dic.sensorapp.databinding.FragmentBeaconBinding
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment
import take.dic.sensorapp.service.RealmManager
import take.dic.sensorapp.service.AvailableSensorManager
import take.dic.sensorapp.value.beacon.BeaconModel
import take.dic.sensorapp.value.beacon.BeaconValue
import java.util.*

class BeaconFragment : BaseBindingFragment(), BeaconConsumer {

    val REQUEST_ENABLE_BLUETOOTH = 4
    private val mBeacon = BeaconValue()
    private var state = ""
    private lateinit var mRegion: Region

    //iBeacon認識のためのフォーマット設定
    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    private lateinit var beaconManager: BeaconManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = bind<FragmentBeaconBinding>(inflater, container, R.layout.fragment_beacon)
        binding.beacon = mBeacon

        beaconManager = BeaconManager.getInstanceForApplication(activity!!)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
        AvailableSensorManager.ble = hasBLESensor()

        return binding.root
    }

    override fun getApplicationContext(): Context = activity!!.applicationContext

    override fun unbindService(p0: ServiceConnection) {
        activity!!.unbindService(p0)
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection, p2: Int): Boolean =
        activity!!.bindService(p0, p1, p2)

    override fun onResume() {
        super.onResume()
        setBluetooth()
        beaconManager.bind(this)
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }

    override fun onBeaconServiceConnect() {
        mRegion = Region("iBeacon", null, null, null)
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
                val currentBeacon = BeaconModel().apply {
                    this.id = UUID.randomUUID().toString()
                    this.major = beacon.id2.toString()
                    this.minor = beacon.id3.toString()
                    this.rssi = beacon.rssi
                    this.distance = beacon.distance
                    this.receivedTime = System.currentTimeMillis()
                }
                Realm.getDefaultInstance().use { realm ->
                    realm.executeTransaction {
                        val model = RealmManager.getRealmModel(realm, currentBeacon)
                        realm.copyToRealm(model)
                    }
                    realm.where(BeaconModel::class.java).findAll()
                        .forEach { Log.e("got", it.toString()) }
                    pushBeaconModel("UNIXTIME: ${currentBeacon.receivedTime}\nmajor: ${currentBeacon.major}, minor: ${currentBeacon.minor}, rssi: ${currentBeacon.rssi}\ndistance: ${currentBeacon.distance}")

                }
            }
        }
        try {
            beaconManager.startRangingBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun pushBeaconModel(element: String) {
        activity!!.runOnUiThread {
            mBeacon.list.add(element)
        }
    }

    private fun setBluetooth() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            val btOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    private fun hasBLESensor(): Boolean {
        val bluetoothManager =
            activity!!.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val mBluetoothAdapter = bluetoothManager.adapter
        return mBluetoothAdapter != null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            AlertDialog.Builder(activity)
                .setTitle("")
                .setMessage("アプリを終了しますか")
                .setPositiveButton("はい") { _, _ ->
                    activity!!.finish()
                    activity!!.moveTaskToBack(true)
                }
                .setNegativeButton("いいえ") { _, _ ->
                    setBluetooth()
                }
                .show()
        }
    }
}