package take.dic.sensorapp.orientation

import android.databinding.ObservableField

data class OrientationData(val title: String, val x: String, val y: String, val z: String) {
    var titleWord = ObservableField(title)
    var xValue = ObservableField(x)
    var yValue = ObservableField(y)
    var zValue = ObservableField(z)
}