package take.dic.sensorapp.fragment.value.gyro

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
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment
import take.dic.sensorapp.R
import take.dic.sensorapp.databinding.FragmentGyroBinding


class GyroSensorFragment : BaseBindingFragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private val gyro = GyroData(title = "角速度", x = "", y = "", z = "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()

        // Listenerの登録 下の二つのgyroのうちのどちらかのコメントアウトを外す

        // ジャイロドリフトの補正があるセンサー出力です。
        val gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        /*ジャイロドリフトの補正がないセンサー出力です。ジャイロドリフトの補正がないと放置しておくとバイアスがかかったよ
          うに徐々にずれていきます。 　
          ただキャリブレーションによる値のジャンプがより滑らかで、この他のセンサー値との合算を考えるケースではこのセンサ
          ー値を使う方が一般的には役に立ちます。*/
        //val gyro = sensorManager!!.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)

        if (gyro != null) {
            sensorManager.registerListener(this, gyro, SensorManager.SENSOR_DELAY_NORMAL)
            // SensorManager.SENSOR_DELAY_NORMALの部分を変えれば周期が変えれる
            // 100000000くらいにすれば見やすいかも
        } else {
            Toast.makeText(context, "角速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = bind<FragmentGyroBinding>(inflater, container, R.layout.fragment_gyro)
        binding.gyro = gyro
        return binding.root
    }


    // 解除するコードも入れる!
    override fun onPause() {
        super.onPause()
        // Listenerを解除
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {

        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            gyro.xValue.set(event.values[0].toString())
            gyro.yValue.set(event.values[1].toString())
            gyro.zValue.set(event.values[2].toString())
        } else if (event.sensor.type == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {
            gyro.xValue.set(event.values[0].toString())
            gyro.yValue.set(event.values[1].toString())
            gyro.zValue.set(event.values[2].toString())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}