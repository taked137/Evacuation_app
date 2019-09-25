package take.dic.sensorapp.fragment.value.motion

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.R
import take.dic.sensorapp.databinding.FragmentAccelerationBinding
import take.dic.sensorapp.fragment.value.base.BaseMotionBindingFragment
import take.dic.sensorapp.service.DeviceInformationManager
import take.dic.sensorapp.value.motion.motions.AccelerationValue

class AccelerationFragment : BaseMotionBindingFragment(), SensorEventListener {
    override val setSensorCondition = { DeviceInformationManager.hasAcceleration = true }
    override val sensorList = mapOf("加速度" to Sensor.TYPE_ACCELEROMETER)
    private lateinit var acceleration: AccelerationValue

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        acceleration = motionValue.acceleration
        val binding =
            bind<FragmentAccelerationBinding>(inflater, container, R.layout.fragment_acceleration)
        binding.acceleration = acceleration
        return binding.root
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            acceleration.setResult(
                System.currentTimeMillis(), event.values[0], event.values[1], event.values[2]
            )
            motionValue.update()
        }
    }
}