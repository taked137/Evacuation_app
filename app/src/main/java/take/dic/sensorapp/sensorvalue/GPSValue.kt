package take.dic.sensorapp.sensorvalue

import android.databinding.ObservableField
import android.location.Location
import android.util.Log
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

// TODO: HUAWEI P10 liteでのデバッグのため、directionが取れてません　他端末でのデバッグ必須
open class GPSValue : RealmObject() {

    @PrimaryKey
    var id = ""

    @Ignore
    val titleWord = ObservableField<String>()
    @Ignore
    val latitudeValue = ObservableField<String>()
    @Ignore
    val longitudeValue = ObservableField<String>()
    @Ignore
    val altitudeValue = ObservableField<String>()
    @Ignore
    val directionValue = ObservableField<String>()

    var unixTime: Long = 0
    var latitude = 0.0
    var longitude = 0.0
    var altitude = 0.0
    var direction = 0f
    var status: Byte = 0x00

    fun setResult(location: Location) {
        this.unixTime = System.currentTimeMillis()
        this.latitudeValue.set(location.latitude.toString())
        latitude = location.latitude
        this.longitudeValue.set(location.longitude.toString())
        longitude = location.longitude
        if(location.hasAltitude()) {
            this.altitudeValue.set(location.altitude.toString())
            altitude = location.altitude
        }
        if(location.hasBearing()) {
            this.directionValue.set(location.bearing.toString())
            direction = location.bearing
        }
    }
}