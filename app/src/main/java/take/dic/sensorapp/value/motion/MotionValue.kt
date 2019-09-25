package take.dic.sensorapp.value.motion

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import take.dic.sensorapp.service.RealmManager
import take.dic.sensorapp.value.motion.motions.AccelerationValue
import take.dic.sensorapp.value.motion.motions.DirectionValue
import take.dic.sensorapp.value.motion.motions.GyroValue
import java.io.Serializable
import java.util.*

open class MotionValue : RealmObject(), Serializable {
    @PrimaryKey
    var id = ""
    var accelerationValue: AccelerationValue? = null
    var gyroValue: GyroValue? = null
    var directionValue: DirectionValue? = null
    var status: Byte = 0x00

    var unixTime: Long = 0

    // PrimaryKeyの関係でRealm格納時にエラーを吐くためRealmモデルとは別にDataBinding用のモデルを用意
    @Ignore
    val acceleration = AccelerationValue().apply {
        this.title.set("加速度")
        this.id = "a"
    }
    @Ignore
    val gyro = GyroValue().apply {
        this.title.set("角速度")
        this.id = "b"
    }
    @Ignore
    val direction = DirectionValue().apply {
        this.title.set("方位")
        this.id = "c"
    }

    private fun saveToRealm(baseModel: List<BaseMotionValue>) {
        val unixTime = System.currentTimeMillis()
        val id = UUID.randomUUID().toString()
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                val model = realm.createObject(MotionValue::class.java, id).apply {
                    this.unixTime = unixTime
                }
                val accelerationValue =
                    RealmManager.getRealmModel<AccelerationValue>(realm, model, baseModel[0])
                val gyroValue =
                    RealmManager.getRealmModel<GyroValue>(realm, model, baseModel[1])
                val directionValue =
                    RealmManager.getRealmModel<DirectionValue>(realm, model, baseModel[2])
                model.apply {
                    this.accelerationValue = accelerationValue
                    this.gyroValue = gyroValue
                    this.directionValue = directionValue
                }
                realm.copyToRealm(model)
            }
        }
    }

    fun update() {
        val motions = listOf(acceleration, gyro, direction)
        if (motions.all { it.isUpdate }) {
            motions.forEach { it.update() }
            saveToRealm(motions)
        }
    }
}