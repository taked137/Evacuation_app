package take.dic.sensorapp

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
import kotlinx.android.synthetic.main.gyro_fragment.*


class GyroSensorFragment : android.support.v4.app.Fragment() , SensorEventListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val args = arguments
        testStr = if (args == null) {
            ""
        } else {
            args.getString(KEY_TEST)
        }
    }

    override fun onResume() {
        super.onResume()

        // Listenerの登録 下の二つのgyroのうちのどちらかのコメントアウトを外す

        // ジャイロドリフトの補正があるセンサー出力です。
        val gyro = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        /*ジャイロドリフトの補正がないセンサー出力です。ジャイロドリフトの補正がないと放置しておくとバイアスがかかったよ
          うに徐々にずれていきます。 　
          ただキャリブレーションによる値のジャンプがより滑らかで、この他のセンサー値との合算を考えるケースではこのセンサ
          ー値を使う方が一般的には役に立ちます。*/
        //val gyro = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)

        if (gyro != null) {
            sensorManager!!.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL)
            // SensorManager.SENSOR_DELAY_NORMALの部分を変えれば周期が変えれる
            // 100000000くらいにすれば見やすいかも
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

    override fun onSensorChanged(event: SensorEvent) {

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            gyroData = GyroData(event.values[0], event.values[1], event.values[2])

            fragment_gyro_x.text = gyroData!!.xGyroData.toString()
            fragment_gyro_y.text = gyroData!!.yGyroData.toString()
            fragment_gyro_z.text = gyroData!!.zGyroData.toString()
        } else if (event.sensor.type == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {
            gyroData = GyroData(event.values[0], event.values[1], event.values[2])

            fragment_gyro_x.text = gyroData!!.xGyroData.toString()
            fragment_gyro_y.text = gyroData!!.yGyroData.toString()
            fragment_gyro_z.text = gyroData!!.zGyroData.toString()
        }

    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }


}