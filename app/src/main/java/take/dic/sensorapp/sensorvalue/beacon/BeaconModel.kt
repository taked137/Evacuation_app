package take.dic.sensorapp.sensorvalue.beacon

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BeaconModel : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var major: String = ""
    var minor: String = ""
    var rssi = 0
    var distance = 0.0
    var receivedTime: Long = 0
    var status: Byte = 0x00
}