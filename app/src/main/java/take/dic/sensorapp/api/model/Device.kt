package take.dic.sensorapp.api.model

import take.dic.sensorapp.service.DeviceInfomationManager

data class Device(
    val id: String = DeviceInfomationManager.id,
    val model: String = DeviceInfomationManager.model,
    val systemVersion: String = DeviceInfomationManager.version,
    val system: String = DeviceInfomationManager.system,
    val availableSensors: AvailableSensors = AvailableSensors(
        DeviceInfomationManager.hasAcceleration,
        DeviceInfomationManager.hasDirection,
        DeviceInfomationManager.hasGyro,
        DeviceInfomationManager.hasGps,
        DeviceInfomationManager.hasBle
    )
)