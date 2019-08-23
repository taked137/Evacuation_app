package take.dic.sensorapp.gps

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import take.dic.sensorapp.BR

class GPSValue(val title : String) {
    val latitude: ObservableField<String> = ObservableField("")
    val longitude: ObservableField<String> = ObservableField("")
    val altitude: ObservableField<String> = ObservableField("")

    fun setResult(latitude : Float, longitude : Float, altitude : Float) {
        this.latitude.set(latitude.toString())
        this.longitude.set(longitude.toString())
        this.altitude.set(altitude.toString())
    }
}