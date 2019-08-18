package take.dic.sensorapp.fragment.value.beacon

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BeaconModel(
    @PrimaryKey
    var id: String = "",
    var major: String = "",
    var minor: String = "",
    var rssi: Int = 0,
    var receivedTime: String = ""
) : RealmObject()