package take.dic.sensorapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.widget.*
import org.altbeacon.beacon.*
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import take.dic.sensorapp.Acceleration.AccelerationSensorFragment
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), BeaconConsumer {
    private lateinit var state: String
    private lateinit var mRegion: Region
    private val beaconList = mutableListOf<BeaconModel>()

    //iBeacon認識のためのフォーマット設定
    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    private lateinit var beaconManager: BeaconManager

    private lateinit var listView: ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private val data = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relative_test)

        //加速度取得用フラグメント
        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.AccelerationContainer, AccelerationSensorFragment.createInstance("渡したい文字列"))
            transaction.commit()
        }

        supportActionBar!!.hide()
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
        listView = findViewById(R.id.listview)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        listView.adapter = arrayAdapter
    }

    override fun onResume(){
        super.onResume()
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
            //count = 0

            //検出したBeaconの情報を全てlog出力
            for (beacon in beacons) {
                val currentBeacon = BeaconModel(beacon.id1, beacon.id2, beacon.id3, beacon.rssi, beacon.txPower, beacon.distance, SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").format(Calendar.getInstance().time))
                beaconList.add(currentBeacon)
                data.add("${currentBeacon.receivedTime}\nmajor: ${currentBeacon.major}, minor: ${currentBeacon.minor}, rssi: ${currentBeacon.rssi}")
            }
            runOnUiThread {
                arrayAdapter.notifyDataSetChanged()
            }
        }
        try {
            beaconManager.startRangingBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }
}