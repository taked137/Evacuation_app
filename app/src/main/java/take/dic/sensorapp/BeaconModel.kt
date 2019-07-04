package take.dic.sensorapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BeaconModel(
    @PrimaryKey
    var id: Int = 0,
    var major: String = "",
    var minor: String = "",
    var rssi: Int = 0,
    var receivedTime: String = ""
) : RealmObject()