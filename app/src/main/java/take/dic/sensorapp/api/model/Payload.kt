package take.dic.sensorapp.api.model

import com.squareup.moshi.Json

data class Payload(
    @field:Json(name = "MotionObjects") val motionObjects: Collection<MotionObject>,
    @field:Json(name = "HeadingObjects") val headingObjects: Collection<HeadingObject>,
    @field:Json(name = "LocationObjects") val locationObjects: Collection<LocationObject>,
    @field:Json(name = "BeaconObjects") val beaconObjects: Collection<BeaconObject>
)

data class MotionObject(
    val acceleration: Motion,
    val direction: Motion,
    val gyro: Motion,
    val receivedTime: String
)

data class Motion(
    val x: String,
    val y: String,
    val z: String
)

data class HeadingObject(
    val direction: String,
    val receivedTime: String
)

data class LocationObject(
    val latitude: String,
    val longitude: String,
    val altitude: String,
    val receivedTime: String
)

data class BeaconObject(
    val beacons: Collection<Beacon>,
    val receivedTime: String
)

data class Beacon(
    val rssi: String,
    val major: String,
    val minor: String,
    val distance: String
)