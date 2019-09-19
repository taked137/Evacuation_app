package take.dic.sensorapp.value

import android.databinding.ObservableField
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class GPSValue : RealmObject() {

    @PrimaryKey
    var id: Long = 0

    @Ignore
    val titleWord = ObservableField<String>()
    @Ignore
    val latitudeValue = ObservableField<String>()
    @Ignore
    val longitudeValue = ObservableField<String>()
    @Ignore
    val altitudeValue = ObservableField<String>()

    var unixTime = ""
    var latitude = 0.0
    var longitude = 0.0
    var altitude = 0.0

    fun setResult(
        unixTime: String, latitudeValue: Double, longitudeValue: Double, altitudeValue: Double
    ) {
        this.unixTime = unixTime
        this.latitudeValue.set(latitudeValue.toString())
        latitude = latitudeValue
        this.longitudeValue.set(longitudeValue.toString())
        longitude = longitudeValue
        this.altitudeValue.set(altitudeValue.toString())
        altitude = altitudeValue
    }
}