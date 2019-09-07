package take.dic.sensorapp.fragment.value.orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment
import take.dic.sensorapp.R
import take.dic.sensorapp.databinding.FragmentOrientationBinding

// TODO: 加速度センサーの種類決定
class OrientationFragment : BaseBindingFragment() , SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private val orientation = OrientationData(title = "方位", x = "", y = "", z = "")

    /** 行列数  */
    private val MATRIX_SIZE = 16
    /** 三次元(XYZ)  */
    private val DIMENSION = 3
    /** センサー管理クラス  */
    private lateinit var mManager: SensorManager
    /** 地磁気行列  */
    private lateinit var mMagneticValues: FloatArray
    /** 加速度行列  */
    private lateinit var mAccelerometerValues: FloatArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        val magnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)//_UNCALIBRATED)

        if (magnet != null) {
            sensorManager.registerListener(this, magnet, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(context, "磁気センサーが存在しません", Toast.LENGTH_LONG).show()
        }

        if (accel != null) {
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(context, "加速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }


        // SensorManager.SENSOR_DELAY_NORMALの部分を変えれば周期が変えれる
        // 100000000くらいにすれば見やすいかも
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = bind<FragmentOrientationBinding>(inflater, container, R.layout.fragment_orientation)
        binding.orientation = orientation
        return binding.root
    }


    // 解除するコードも入れる!
    override fun onPause() {
        super.onPause()
        // Listenerを解除
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {

        // センサーイベント
        when (event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD ->
                // 地磁気センサー
                mMagneticValues = event.values.clone()
            Sensor.TYPE_ACCELEROMETER -> //_UNCALIBRATED ->
                // 加速度センサー
                mAccelerometerValues = event.values.clone()
            else ->
                // それ以外は無視
                return
        }
        if (this::mMagneticValues.isInitialized && this::mAccelerometerValues.isInitialized) {
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

            orientation.unixTime.set(System.currentTimeMillis().toString())
            orientation.xValue.set(radianToDegrees(orientationValues[1]).toString())
            orientation.yValue.set(radianToDegrees(orientationValues[2]).toString())
            orientation.zValue.set(radianToDegrees(orientationValues[0]).toString())
        }
    }

    private fun radianToDegrees(angrad : Float) : Float{
        return if (angrad >= 0 ) {
            Math.toDegrees(angrad.toDouble()).toFloat()
        } else {
            (360 + Math.toDegrees(angrad.toDouble())).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }
}