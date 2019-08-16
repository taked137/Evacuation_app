package take.dic.sensorapp.gps

import android.databinding.ObservableField

data class GPSData(val title: String, val latitude: String, val longitude: String) {
    var titleWord = ObservableField(title)
    var latitudeValue = ObservableField(latitude)
    var longitudeValue = ObservableField(longitude)
}