package take.dic.sensorapp.value

import android.databinding.ObservableField

data class GPSValue(val title: String, val latitude: String, val longitude: String, val altitude: String) {
    var titleWord = ObservableField(title)
    var unixTime = ""
    var latitudeValue = ObservableField(latitude)
    var longitudeValue = ObservableField(longitude)
    var altitudeValue = ObservableField(altitude)

    fun setResult(unixTime: String, latitudeValue: Float, longitudeValue: Float, altitudeValue: Float){
        this.unixTime = unixTime
        this.latitudeValue.set(latitudeValue.toString())
        this.longitudeValue.set(longitudeValue.toString())
        this.altitudeValue.set(altitudeValue.toString())
    }
}