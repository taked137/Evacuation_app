package take.dic.sensorapp.fragment.value.motion

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.R
import take.dic.sensorapp.databinding.FragmentGyroBinding
import take.dic.sensorapp.fragment.value.base.BaseMotionBindingFragment
import take.dic.sensorapp.service.DeviceInformationManager
import take.dic.sensorapp.sensorvalue.motion.motions.GyroValue

class GyroFragment : BaseMotionBindingFragment(), SensorEventListener {
    override val setSensorCondition = { DeviceInformationManager.hasGyro = true }
    override val sensorList = mapOf("角速度" to Sensor.TYPE_GYROSCOPE)
    private lateinit var gyro: GyroValue

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        gyro = motionValue.gyro
        val binding = bind<FragmentGyroBinding>(inflater, container, R.layout.fragment_gyro)
        binding.gyro = gyro
        return binding.root
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE || event.sensor.type == Sensor.TYPE_GYROSCOPE_UNCALIBRATED) {
            gyro.setResult(
                System.currentTimeMillis(),
                event.values[0],
                event.values[1],
                event.values[2]
            )
            motionValue.update()
        }
    }
}