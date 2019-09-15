package take.dic.sensorapp.value.motion

import android.databinding.ObservableField

abstract class BaseMotionValue(title: String) {

    val title = ObservableField(title)
    val x = ObservableField("")
    val y = ObservableField("")
    val z = ObservableField("")

    var unixTime = ""
    var newX = 0f
    var newY = 0f
    var newZ = 0f

    var isUpdate: Boolean = false

    fun setResult(unixTime: String, newX: Float, newY: Float, newZ: Float) {
        this.unixTime = unixTime
        this.newX = newX
        this.newY = newY
        this.newZ = newZ
        isUpdate = true
    }

    fun update() {
        x.set(newX.toString())
        y.set(newY.toString())
        z.set(newZ.toString())
        isUpdate = false
    }
}