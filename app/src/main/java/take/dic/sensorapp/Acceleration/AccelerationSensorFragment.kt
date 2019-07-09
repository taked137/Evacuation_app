package take.dic.sensorapp.Acceleration

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import kotlinx.android.synthetic.main.acceleration_fragment.*
import take.dic.sensorapp.R


class AccelerationSensorFragment : android.support.v4.app.Fragment() , SensorEventListener {

    private var testStr: String? = null //MainActivityから受け取る文字列(何かあれば)
    private var sensorManager: SensorManager? = null


    companion object {
        var accelerationData : AccelerationData? = null //加速度データが収納される

        private const val KEY_TEST = "test"

        fun createInstance(testStr: String) : AccelerationSensorFragment {
            val accelerationFragment = AccelerationSensorFragment()
            val args = Bundle()
            args.putString(KEY_TEST, testStr)
            accelerationFragment.arguments = args
            return accelerationFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val args = arguments
        if (args == null) {
            testStr = ""
        } else {
            testStr = args.getString(KEY_TEST)
        }
    }

    override fun onResume() {
        super.onResume()
        // Listenerの登録
        val accel = sensorManager!!.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )

        if (accel != null) {
            sensorManager!!.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
            // SensorManager.SENSOR_DELAY_NORMALの部分を変えれば周期が変えれる
            // 100000000くらいにすれば見やすいかも

            //sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
            //sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
            //sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(context, "加速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.acceleration_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    // 解除するコードも入れる!
    override fun onPause() {
        super.onPause()
        // Listenerを解除
        sensorManager!!.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent) {


        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {

            accelerationData = AccelerationData(event.values[0], event.values[1], event.values[2],"")

            fragment_accel_x.text = accelerationData!!.xAcceleration.toString()
            fragment_accel_y.text = accelerationData!!.yAcceleration.toString()
            fragment_accel_z.text = accelerationData!!.zAcceleration.toString()

            showInfo(event)
        }
    }

    // （お好みで）加速度センサーの各種情報を表示
    private fun showInfo(event: SensorEvent) {
        // センサー名
        val info = StringBuffer("Name: ")
        info.append(event.sensor.name)
        info.append("\n")

        // ベンダー名
        info.append("Vendor: ")
        info.append(event.sensor.vendor)
        info.append("\n")

        // 型番
        info.append("Type: ")
        info.append(event.sensor.type)
        info.append("\n")

        // 最小遅れ
        var data = event.sensor.minDelay
        info.append("Mindelay: ")
        info.append(data.toString())
        info.append(" usec\n")

        // 最大遅れ
        data = event.sensor.maxDelay
        info.append("Maxdelay: ")
        info.append(data.toString())
        info.append(" usec\n")

        // レポートモード
        data = event.sensor.reportingMode
        var stinfo = "unknown"
        if (data == 0) {
            stinfo = "REPORTING_MODE_CONTINUOUS"
        } else if (data == 1) {
            stinfo = "REPORTING_MODE_ON_CHANGE"
        } else if (data == 2) {
            stinfo = "REPORTING_MODE_ONE_SHOT"
        }
        info.append("ReportingMode: ")
        info.append(stinfo)
        info.append("\n")

        // 最大レンジ
        info.append("MaxRange: ")
        var fData = event.sensor.maximumRange
        info.append(fData.toString())
        info.append("\n")

        // 分解能
        info.append("Resolution: ")
        fData = event.sensor.resolution
        info.append(fData.toString())
        info.append(" m/s^2\n")

        // 消費電流
        info.append("Power: ")
        fData = event.sensor.power
        info.append(fData.toString())
        info.append(" mA\n")

        //text_info!!.text = info
        accelerationData!!.sensorInfo = info.toString()
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }


}