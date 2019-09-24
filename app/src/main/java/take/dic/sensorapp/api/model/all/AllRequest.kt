package take.dic.sensorapp.api.model.all

import take.dic.sensorapp.api.model.Device
import take.dic.sensorapp.api.model.Payload

data class AllRequest(
    val since: String? = null,
    val created: String = System.currentTimeMillis().toString(),
    val device: Device,
    val payload: Payload
)