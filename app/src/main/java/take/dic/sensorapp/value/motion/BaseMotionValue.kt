package take.dic.sensorapp.value.motion

import android.databinding.ObservableField
import io.realm.RealmModel

// Realmがクラスの継承をサポートしていないため、仕方なくインタフェースで実装
interface BaseMotionValue : RealmModel {
    var id: String

    val title: ObservableField<String>
    val xValue: ObservableField<String>
    val yValue: ObservableField<String>
    val zValue: ObservableField<String>

    var unixTime: Long
    var x: Float
    var y: Float
    var z: Float

    var motionValue: MotionValue?

    var isUpdate: Boolean

    fun setResult(unixTime: Long, x: Float, y: Float, z: Float) {
        this.unixTime = unixTime
        this.x = x
        this.y = y
        this.z = z
        isUpdate = true
    }

    fun update() {
        xValue.set(x.toString())
        yValue.set(y.toString())
        zValue.set(z.toString())
        isUpdate = false
    }
}