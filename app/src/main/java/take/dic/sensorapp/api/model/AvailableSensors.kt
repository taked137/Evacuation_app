package take.dic.sensorapp.api.model

data class AvailableSensors(
    val acceleration: Boolean,
    val direction: Boolean,
    val gyro: Boolean,
    val gps: Boolean,
    val ble: Boolean
)