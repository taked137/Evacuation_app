package take.dic.sensorapp.value.motion

import java.io.Serializable

class MotionValue(accelerationTitle: String, gyroTitle: String, directionTitle: String) :
    Serializable {
    val accelerationValue = AccelerationValue(accelerationTitle)
    val gyroValue = GyroValue(gyroTitle)
    val directionValue = DirectionValue(directionTitle)

    private val motions = listOf(accelerationValue, gyroValue, directionValue)

    fun update() {
        if (motions.all { it.isUpdate }) {
            motions.forEach { it.update() }
        }
    }
}