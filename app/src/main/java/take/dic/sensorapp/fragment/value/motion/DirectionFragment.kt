package take.dic.sensorapp.fragment.value.motion

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.R
import take.dic.sensorapp.databinding.FragmentDirectionBinding
import take.dic.sensorapp.fragment.value.base.BaseMotionBindingFragment
import take.dic.sensorapp.value.motion.BaseMotionValue
import take.dic.sensorapp.value.motion.motions.DirectionValue

// TODO: 加速度センサーの種類決定
class DirectionFragment : BaseMotionBindingFragment(), SensorEventListener {
    override val sensorList =
        mapOf("磁気" to Sensor.TYPE_MAGNETIC_FIELD, "加速度" to Sensor.TYPE_ACCELEROMETER)
    private lateinit var direction: DirectionValue

    /** 行列数  */
    private val MATRIX_SIZE = 16
    /** 三次元(XYZ)  */
    private val DIMENSION = 3
    /** 地磁気行列  */
    private lateinit var mMagneticValues: FloatArray
    /** 加速度行列  */
    private lateinit var mAccelerometerValues: FloatArray

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        direction = motionValue.direction
        val binding =
            bind<FragmentDirectionBinding>(inflater, container, R.layout.fragment_direction)
        binding.direction = direction
        return binding.root
    }

    override fun onSensorChanged(event: SensorEvent) {
        // センサーイベント
        when (event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD ->
                // 地磁気センサー
                mMagneticValues = event.values.clone()
            Sensor.TYPE_ACCELEROMETER ->
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
            SensorManager.getRotationMatrix(
                rotationMatrix,
                inclinationMatrix,
                mAccelerometerValues,
                mMagneticValues
            )
            SensorManager.remapCoordinateSystem(
                rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remapedMatrix
            )
            SensorManager.getOrientation(remapedMatrix, orientationValues)

            direction.setResult(
                System.currentTimeMillis(),
                radianToDegrees(orientationValues[1]),
                radianToDegrees(orientationValues[2]),
                radianToDegrees(orientationValues[0])
            )
            motionValue.update()
        }
    }

    private fun radianToDegrees(angrad: Float): Float {
        return if (angrad >= 0) {
            Math.toDegrees(angrad.toDouble()).toFloat()
        } else {
            (360 + Math.toDegrees(angrad.toDouble())).toFloat()
        }
    }
}