package take.dic.sensorapp


import android.databinding.ObservableField


open class Motion(val title : String) {
    val x: ObservableField<String> = ObservableField("")
    val y: ObservableField<String> = ObservableField("")
    val z: ObservableField<String> = ObservableField("")

    var newX : Float = 0f
    var newY : Float = 0f
    var newZ : Float = 0f

    fun setResult(newX : Float, newY : Float, newZ : Float) {
        this.newX = newX
        this.newY = newY
        this.newZ = newZ
    }

    fun update() {
        x.set(newX.toString())
        y.set(newY.toString())
        z.set(newZ.toString())

        /*when (newZ) {
            in 45..135 -> {
                z.set("$newZ 西")
            }
            in 135..225 -> {
                z.set("$newZ 南")
            }
            in 225..315 -> {
                z.set("$newZ 東")
            }
            else -> {
                z.set("$newZ 北")
            }
        }*/
    }
}