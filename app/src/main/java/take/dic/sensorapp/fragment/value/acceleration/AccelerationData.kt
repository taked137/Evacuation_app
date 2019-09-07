package take.dic.sensorapp.fragment.value.acceleration

import android.databinding.ObservableField

data class AccelerationData(val title: String, val x: String, val y: String, val z: String) {
    var titleWord = ObservableField(title)
    var unixTime = ObservableField<String>()
    var xValue = ObservableField(x)
    var yValue = ObservableField(y)
    var zValue = ObservableField(z)
}