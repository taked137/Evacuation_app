package take.dic.sensorapp.service

import io.realm.Realm
import io.realm.RealmResults
import take.dic.sensorapp.api.model.*
import take.dic.sensorapp.value.GPSValue
import take.dic.sensorapp.value.beacon.BeaconModel
import take.dic.sensorapp.value.motion.BaseMotionValue
import take.dic.sensorapp.value.motion.MotionValue
import java.util.*

object RealmManager {
    fun getRealmModel(realm: Realm, model: GPSValue) =
        realm.createObject(GPSValue::class.java, UUID.randomUUID().toString()).apply {
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

    fun getMotionObjects(): List<MotionObject> {
        val list = LinkedList<MotionObject>()
        Realm.getDefaultInstance().use { realm ->
            val motions = realm.where(MotionValue::class.java).findAll()
            motions.forEach { list.add(extractMotionObject(it)) }
        }
        return list
    }

    private fun extractMotionObject(motion: MotionValue): MotionObject = MotionObject(
        Motion(
            motion.acceleration.x.toString(),
            motion.acceleration.y.toString(),
            motion.acceleration.z.toString()
        ),
        Motion(
            motion.direction.x.toString(),
            motion.direction.y.toString(),
            motion.direction.z.toString()
        ),
        Motion(
            motion.gyro.x.toString(),
            motion.gyro.y.toString(),
            motion.gyro.z.toString()
        ),
        motion.unixTime.toString()
    )

    fun getLocationObjects(): List<LocationObject> {
        val list = LinkedList<LocationObject>()
        Realm.getDefaultInstance().use { realm ->
            val locations = realm.where(GPSValue::class.java).findAll()
            locations.forEach { list.add(extractLocationObject(it)) }
        }
        return list
    }

    private fun extractLocationObject(location: GPSValue): LocationObject =
        LocationObject(
            location.latitude.toString(),
            location.longitude.toString(),
            location.altitude.toString(),
            location.unixTime.toString()
        )

    fun getBeaconObjects(): List<BeaconObject> {
        val list = LinkedList<BeaconObject>()
        Realm.getDefaultInstance().use { realm ->
            while (true) {
                realm.beginTransaction()
                val beacon = realm.where(BeaconModel::class.java).findFirst() ?: break
                val stBeacons = realm.where(BeaconModel::class.java)
                    .equalTo("receivedTime", beacon.receivedTime).findAll()
                list.add(extractBeaconObject(beacon, stBeacons))
                beacon.deleteFromRealm()
                stBeacons.forEach { it.deleteFromRealm() }
                realm.commitTransaction()
            }
        }
        return list
    }

    private fun extractBeaconObject(
        beacon: BeaconModel, stBeacons: RealmResults<BeaconModel>
    ): BeaconObject {
        val list = LinkedList<Beacon>()
        list.add(extractBeacon(beacon))
        stBeacons.forEach { list.add(extractBeacon(it)) }
        return BeaconObject(list, beacon.receivedTime.toString())
    }

    private fun extractBeacon(beacon: BeaconModel) =
        Beacon(beacon.rssi.toString(), beacon.major, beacon.minor, beacon.distance.toString())
}