package take.dic.sensorapp.fragment.value.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import take.dic.sensorapp.value.motion.MotionValue

abstract class BaseMotionBindingFragment : BaseBindingFragment(), SensorEventListener {
    abstract val sensorList: Map<String, Int>
    lateinit var sensorManager: SensorManager
    lateinit var motionValue: MotionValue

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): View

    private fun registerListener(sensorManager: SensorManager) {
        sensorList.forEach {
            val sensor = sensorManager.getDefaultSensor(it.value)
            if (sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            } else {
                Toast.makeText(context, "${it.key}センサーが存在しません", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        motionValue = arguments!!.getSerializable("motionValue") as MotionValue
        sensorManager = activity!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return getBinding(inflater, container)
    }

    override fun onResume() {
        super.onResume()
        registerListener(sensorManager)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}