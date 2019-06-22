package take.dic.sensorapp

import org.altbeacon.beacon.Identifier

data class BeaconModel(
    val uuid: Identifier,
    val major: Identifier,
    val minor: Identifier,
    val rssi: Int,
    val txPower: Int,
    val distance: Double,
    val receivedTime: String
)