
import android.databinding.ObservableField


/*
このクラスは使っていない。代わりにMotionを使用している
Motionをabstractにしてこれに継承させるかも
 */

class AccelerationValue {
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
        isUpdate = true
    }

    fun update() {
        x.set(newX.toString())
        y.set(newY.toString())
        z.set(newZ.toString())
    }
}