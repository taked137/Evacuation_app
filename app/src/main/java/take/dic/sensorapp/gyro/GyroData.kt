package take.dic.sensorapp.gyro

import android.databinding.ObservableField

data class GyroData(val title: String, val x: String, val y: String, val z: String) {
    var titleWord = ObservableField(title)
    var xValue = ObservableField(x)
    var yValue = ObservableField(y)
    var zValue = ObservableField(z)
}