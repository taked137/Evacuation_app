package take.dic.sensorapp.sensorvalue.motion.motions

import android.databinding.ObservableField
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import take.dic.sensorapp.sensorvalue.motion.BaseMotionValue
import take.dic.sensorapp.sensorvalue.motion.MotionValue

@RealmClass
open class AccelerationValue : BaseMotionValue {
    @PrimaryKey
    override var id = ""
    @Ignore
    override val title = ObservableField<String>()
    @Ignore
    override val xValue = ObservableField<String>()
    @Ignore
    override val yValue = ObservableField<String>()
    @Ignore
    override val zValue = ObservableField<String>()

    override var unixTime: Long = 0
    override var x = 0f
    override var y = 0f
    override var z = 0f

    override var motionValue: MotionValue? = null

    @Ignore
    override var isUpdate = false
}