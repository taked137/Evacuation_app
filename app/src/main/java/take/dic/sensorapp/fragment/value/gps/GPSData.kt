package take.dic.sensorapp.fragment.value.gps

import android.databinding.ObservableField

data class GPSData(val title: String, val latitude: String, val longitude: String, val altitude: String) {
    var titleWord = ObservableField(title)
    var unixTime = ObservableField<String>()
    var latitudeValue = ObservableField(latitude)
    var longitudeValue = ObservableField(longitude)
    var altitudeValue = ObservableField(altitude)
}