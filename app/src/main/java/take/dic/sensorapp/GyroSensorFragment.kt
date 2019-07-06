package take.dic.sensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.gyro_fragment.*


class GyroSensorFragment : android.support.v4.app.Fragment() , SensorEventListener {


    var list : MutableList<String>? = null
    private var testStr: String? = null //MainActivityから受け取る文字列(何かあれば)
    private var sensorManager: SensorManager? = null


    companion object {
        var gyroData : GyroData? = null //角速度データが収納される

        private const val KEY_TEST = "test"

        fun createInstance(testStr: String) : GyroSensorFragment {
            val gyroFragment = GyroSensorFragment()
            val args = Bundle()
            args.putString(KEY_TEST, testStr)
            gyroFragment.arguments = args
            return gyroFragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
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
        val gyro = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (gyro != null) {
            sensorManager!!.registerListener(this, gyro, SensorManager.SENSOR_DELAY_UI)
        } else {
            Toast.makeText(context, "角速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.gyro_fragment, container, false)
    }


    // 解除するコードも入れる!
    override fun onPause() {
        super.onPause()
        // Listenerを解除
        sensorManager!!.unregisterListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onSensorChanged(event: SensorEvent) {

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            gyroData = GyroData(event.values[0], event.values[1], event.values[2])

            fragment_gyro_x.text = gyroData!!.xGyroData.toString()
            fragment_gyro_y.text = gyroData!!.yGyroData.toString()
            fragment_gyro_z.text = gyroData!!.zGyroData.toString()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }


}