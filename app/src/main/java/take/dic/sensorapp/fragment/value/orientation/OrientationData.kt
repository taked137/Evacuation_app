package take.dic.sensorapp.fragment.value.orientation

import android.databinding.ObservableField

data class OrientationData(val title: String, val x: String, val y: String, val z: String) {
    var titleWord = ObservableField(title)
    var unixTime = ObservableField<String>()
    var xValue = ObservableField(x)
    var yValue = ObservableField(y)
    var zValue = ObservableField(z)
}