package take.dic.sensorapp

import android.os.Bundle
import android.os.RemoteException
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import io.realm.Realm
import org.altbeacon.beacon.*
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
    private lateinit var realm: Realm
    private val data = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relative_test)

        supportActionBar!!.hide()
        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
        listView = findViewById(R.id.listview)
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
        listView.adapter = arrayAdapter

        Realm.init(this)
    }

    override fun onResume() {
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

            //検出したBeaconの情報を全て出力
            for (beacon in beacons) {
                realm = Realm.getDefaultInstance()
                var id: Int = try {
                    realm.where(BeaconModel::class.java).findAll().size + 1
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
                realm.executeTransaction {
                    val model = realm.createObject(BeaconModel::class.java!!, id)
                    model.major = currentBeacon.major
                    model.minor = currentBeacon.minor
                    model.rssi = currentBeacon.rssi
                    model.receivedTime = currentBeacon.receivedTime
                    realm.copyToRealm(model)
                }
                realm.where(BeaconModel::class.java).findAll().forEach { Log.e("got", it.toString()) }

                data.add(0, "${currentBeacon.receivedTime}\nmajor: ${currentBeacon.major}, minor: ${currentBeacon.minor}, rssi: ${currentBeacon.rssi}")
                realm.close()
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

    // 試験用(削除予定)
    override fun onStop() {

        realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.delete(BeaconModel::class.java)
        realm.commitTransaction()
        realm.close()

        super.onStop()
    }
}