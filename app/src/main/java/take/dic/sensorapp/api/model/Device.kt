package take.dic.sensorapp.api.model

import take.dic.sensorapp.service.DeviceInformationManager

data class Device(
    val id: String = DeviceInformationManager.id,
    val model: String = DeviceInformationManager.model,
    val systemVersion: String = DeviceInformationManager.version,
    val system: String = DeviceInformationManager.system,
    val availableSensors: AvailableSensors = AvailableSensors(
        DeviceInformationManager.hasAcceleration,
        DeviceInformationManager.hasDirection,
        DeviceInformationManager.hasGyro,
        DeviceInformationManager.hasGps,
        DeviceInformationManager.hasBle
    )
)