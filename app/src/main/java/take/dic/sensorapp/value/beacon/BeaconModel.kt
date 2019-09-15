package take.dic.sensorapp.value.beacon

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BeaconModel(
    @PrimaryKey
    var id: String = "",
    var major: String = "",
    var minor: String = "",
    var rssi: Int = 0,
    var distance: Double = 0.0,
    var receivedTime: Long = 0
) : RealmObject()