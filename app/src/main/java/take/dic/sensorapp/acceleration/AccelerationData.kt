package take.dic.sensorapp.acceleration

import android.databinding.ObservableField

data class AccelerationData(val title: String, var x: String, var y: String, var z: String) {
    var xValue = ObservableField(x)
    var yValue = ObservableField(y)
    var zValue = ObservableField(z)
}