package take.dic.sensorapp.angular

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableField
import android.databinding.ObservableFloat
import take.dic.sensorapp.BR

/*
このクラスは使っていない。代わりにMotionを使用している
Motionをabstractにしてこれに継承させるかも
 */
class AngularValue(val title : String) {
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
    }
}