package take.dic.sensorapp.api.model.regular

import take.dic.sensorapp.api.model.Device
import take.dic.sensorapp.api.model.Payload

data class RegularRequest(
    val created: String = System.currentTimeMillis().toString(),
    val device: Device,
    val payload: Payload
)