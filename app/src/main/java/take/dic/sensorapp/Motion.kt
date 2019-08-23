package take.dic.sensorapp


import android.databinding.ObservableField


open class Motion(val title : String) {
    val x: ObservableField<String> = ObservableField("")
    val y: ObservableField<String> = ObservableField("")
    val z: ObservableField<String> = ObservableField("")

    var newX : Float = 0f
    var newY : Float = 0f
    var newZ : Float = 0f

    var isUpdate: Boolean = false

    fun setResult(x : Float, y : Float, z : Float) {
        newX = x
        newY = y
        newZ = z
        this.x.set(newX.toString())
        isUpdate = true
    }

    fun update() {
        x.set(newX.toString())
        y.set(newY.toString())
        z.set(newZ.toString())
        isUpdate = false
    }
}