package take.dic.sensorapp.service

import io.realm.Realm
import take.dic.sensorapp.value.GPSValue
import take.dic.sensorapp.value.beacon.BeaconModel
import take.dic.sensorapp.value.motion.BaseMotionValue
import take.dic.sensorapp.value.motion.MotionValue
import java.util.*

object RealmManager {
    fun getRealmModel(realm: Realm, model: GPSValue) = realm.createObject(GPSValue::class.java, UUID.randomUUID().toString()).apply {
        this.latitude = model.latitude
        this.longitude = model.longitude
        this.altitude = model.altitude
        this.unixTime = model.unixTime
    }!!

    fun getRealmModel(realm: Realm, model: BeaconModel) =
        realm.createObject(BeaconModel::class.java, model.id).apply {
            this.major = model.major
            this.minor = model.minor
            this.rssi = model.rssi
            this.receivedTime = model.receivedTime
            this.distance = model.distance
        }!!

    inline fun <reified T : BaseMotionValue> getRealmModel(
        realm: Realm, motionValue: MotionValue, model: BaseMotionValue
    ) = realm.createObject(T::class.java, motionValue.id).apply {
        this.unixTime = motionValue.unixTime
        this.x = model.x
        this.y = model.y
        this.z = model.z
        this.motionValue = motionValue
    }!!
}