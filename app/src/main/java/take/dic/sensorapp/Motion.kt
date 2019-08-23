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

    fun setResult(newX : Float, newY : Float, newZ : Float) {
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