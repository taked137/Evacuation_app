package take.dic.sensorapp

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.location.*
import io.realm.Realm
import org.altbeacon.beacon.*
import take.dic.sensorapp.beacon.BeaconModel
import take.dic.sensorapp.beacon.BeaconValue
import take.dic.sensorapp.databinding.FragmentValueBinding
import take.dic.sensorapp.gps.GPSValue
import java.text.SimpleDateFormat
import java.util.*

class ValueFragment : Fragment(), BeaconConsumer, SensorEventListener {

    val REQUEST_ENABLE_BLUETOOTH = 4

    //var num = 0 // GPSの間隔試すための変数

    private val gps = GPSValue("GPS")

    private val motionValue = MotionValue()
    private val mBeacon = BeaconValue()
    private var state = ""
    private lateinit var mRegion: Region
    private lateinit var realm: Realm

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var sensorManager: SensorManager? = null
    // 行列数
    private val MATRIX_SIZE = 16
    // 三次元(XYZ)
    private val DIMENSION = 3

    // 地磁気行列
    private var mMagneticValues: FloatArray? = null
    // 加速度行列
    private var mAccelerometerValues: FloatArray? = null

    //iBeacon認識のためのフォーマット設定
    private val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(Build.VERSION.SDK_INT >= 23){
            checkPermission()
        } else {
            locationStart()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentValueBinding.inflate(inflater, container, false)

        if( ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setBluetooth()

            binding.gps = gps

            binding.acceleration = motionValue.motions[0]
            binding.angular = motionValue.motions[1]
            binding.orientation = motionValue.motions[2]

            binding.beacon = mBeacon

            beaconManager = BeaconManager.getInstanceForApplication(activity!!)
            beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))


            //加速度・角速度・磁気センサー
            sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        Realm.init(activity!!)
        return binding.root
    }

    override fun getApplicationContext(): Context = activity!!.applicationContext

    override fun unbindService(p0: ServiceConnection) {
        activity!!.unbindService(p0)
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection, p2: Int): Boolean = activity!!.bindService(p0, p1, p2)

    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)

        //加速度・角速度・磁気センサー

        val accelSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val gyroSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        val magnetSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (sensorManager == null) {
            sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        if (accelSensor != null) {
            sensorManager!!.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            motionValue.motions[0] = null //motionValue.motions[0]は加速度データ。センサがない場合はこのデータは使わない
            Toast.makeText(context, "加速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }

        if (gyroSensor != null) {
            sensorManager!!.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            motionValue.motions[1] = null //motionValue.motions[1]は角速度データ。センサがない場合はこのデータは使わない
            Toast.makeText(context, "角速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }

        if (magnetSensor != null) {
            sensorManager!!.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            motionValue.motions[2] = null //motionValue.motions[2]は方位データ。センサがない場合はこのデータは使わない
            Toast.makeText(context, "磁気センサーが存在しません", Toast.LENGTH_LONG).show()
        }

    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)

        // Listenerを解除
        sensorManager!!.unregisterListener(this)
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


    /*取得したデータの種類に応じてmotionValue.motions内の加速度・角速度・方位データを(一時的に)保存
    同時更新じたいのでmotionValue.updateData()を最後にする*/
    override fun onSensorChanged(event: SensorEvent) {

        //加速度センサー
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            if(motionValue.motions[0] != null) { //
                motionValue.motions[0]!!.setResult(event.values[0], event.values[1], event.values[2])
                mAccelerometerValues = event.values.clone()
            }
        //角速度センサー
        } else if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
             if(motionValue.motions[1] != null) {
                 motionValue.motions[1]!!.setResult(event.values[0], event.values[1], event.values[2])
             }
            motionValue.updateData() //三つのセンサーの値を同時に更新。角速度情報更新の時を基準にする

        //磁気センサー
        } else {
            mMagneticValues = event.values.clone()
        }

        //加速度・磁気データから方位をデータを取得
        if (mMagneticValues != null && mAccelerometerValues != null) {
            val rotationMatrix = FloatArray(MATRIX_SIZE)
            val inclinationMatrix = FloatArray(MATRIX_SIZE)
            val remapedMatrix = FloatArray(MATRIX_SIZE)
            val orientationValues = FloatArray(DIMENSION)
            // 加速度センサーと地磁気センサーから回転行列を取得
            SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix, mAccelerometerValues, mMagneticValues)
            SensorManager.remapCoordinateSystem(
                rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remapedMatrix
            )
            SensorManager.getOrientation(remapedMatrix, orientationValues)


            //zが方位を表す
            if(motionValue.motions[2] != null) {
                motionValue.motions[2]!!.setResult(
                    radianToDegrees(orientationValues[1]),
                    radianToDegrees(orientationValues[2]),
                    radianToDegrees(orientationValues[0])
                )
            }

        }

    }

    private fun radianToDegrees(angrad : Float) : Float{
        return if (angrad >= 0 ) {
            Math.toDegrees(angrad.toDouble()).toFloat()
        } else {
            (360 + Math.toDegrees(angrad.toDouble())).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}



    // GPS用のメソッド群

    private fun locationStart() {

        Log.d("debug", "locationStart()")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)

        val locationRequest = LocationRequest()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                // 更新直後の位置が格納されているはず
                val location = locationResult?.lastLocation ?: return
                gps.latitude.set(location.latitude.toString())// + " ${num++}回目")
                gps.longitude.set(location.longitude.toString() )
                gps.altitude.set(location.altitude.toString())
            }
        }

        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 1
        locationRequest.fastestInterval = 1
        fusedLocationClient!!.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    // 使用可能かどうかの判定後、無許可なら許可を申請する
    private fun checkPermission(){
        val judge = ( ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

        if (judge) {
            locationStart()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000)
        }
    }

    // 許可申請後の処理
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
                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS z").format(Calendar.getInstance().time)
                )
                realm.executeTransaction {
                    val model = realm.createObject(BeaconModel::class.java, id)
                    model.major = currentBeacon.major
                    model.minor = currentBeacon.minor
                    model.rssi = currentBeacon.rssi
                    model.receivedTime = currentBeacon.receivedTime
                    realm.copyToRealm(model)
                }
                realm.where(BeaconModel::class.java).findAll().forEach { Log.e("got", it.toString()) }
                pushBeaconModel("${currentBeacon.receivedTime}\nmajor: ${currentBeacon.major}, minor: ${currentBeacon.minor}, rssi: ${currentBeacon.rssi}")

                realm.close()
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
            mBeacon.list.add(0, element)
        }
    }

    private fun setBluetooth() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            val btOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(btOn, REQUEST_ENABLE_BLUETOOTH)
        }
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