package take.dic.sensorapp

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
import kotlinx.android.synthetic.main.orientation_fragment.*


class OrientationFragment : android.support.v4.app.Fragment() , SensorEventListener {

    private var testStr: String? = null //MainActivityから受け取る文字列(何かあれば)
    private var sensorManager: SensorManager? = null

    /** 行列数  */
    private val MATRIX_SIZE = 16
    /** 三次元(XYZ)  */
    private val DIMENSION = 3
    /** センサー管理クラス  */
    private val mManager: SensorManager? = null
    /** 地磁気行列  */
    private var mMagneticValues: FloatArray? = null
    /** 加速度行列  */
    private var mAccelerometerValues: FloatArray? = null


    companion object {
        var orientationData : OrientationData? = null //方位データが収納される

        private const val KEY_TEST = "test"

        fun createInstance(testStr: String) : OrientationFragment {
            val orientationFragment = OrientationFragment()
            val args = Bundle()
            args.putString(KEY_TEST, testStr)
            orientationFragment.arguments = args
            return orientationFragment
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        val magnet = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val accel = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED)

        if (sensorManager == null) {
            sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }

        if (magnet != null) {
            sensorManager!!.registerListener(this, magnet, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(context, "磁気センサーが存在しません", Toast.LENGTH_LONG).show()
        }

        if (accel != null) {
            sensorManager!!.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(context, "加速度センサーが存在しません", Toast.LENGTH_LONG).show()
        }


        // SensorManager.SENSOR_DELAY_NORMALの部分を変えれば周期が変えれる
        // 100000000くらいにすれば見やすいかも
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.orientation_fragment, container, false)
    }


    // 解除するコードも入れる!
    override fun onPause() {
        super.onPause()
        // Listenerを解除
        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {

        // センサーイベント
        when (event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD ->
                // 地磁気センサー
                mMagneticValues = event.values.clone()
            Sensor.TYPE_ACCELEROMETER_UNCALIBRATED ->
                // 加速度センサー
                mAccelerometerValues = event.values.clone()
            else ->
                // それ以外は無視
                return
        }
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

            orientationData = OrientationData(
                radianToDegrees(orientationValues[1]),
                radianToDegrees(orientationValues[2]),
                radianToDegrees(orientationValues[0])
            )

            fragment_orientation_x.text = orientationData!!.xAttitude.toString()
            fragment_orientation_y.text = orientationData!!.yAttitude.toString()
            fragment_orientation_z.text = orientationData!!.zAttitude.toString()
        }

    }

    private fun radianToDegrees(angrad : Float) : Float{
        if (angrad >= 0 ) {
            return Math.toDegrees(angrad.toDouble()).toFloat()
        } else {
            return (360 + Math.toDegrees(angrad.toDouble())).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }


}