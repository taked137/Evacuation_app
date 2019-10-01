package take.dic.sensorapp.service

import io.realm.Realm
import take.dic.sensorapp.api.model.*
import take.dic.sensorapp.value.GPSValue
import take.dic.sensorapp.value.beacon.BeaconModel
import take.dic.sensorapp.value.motion.BaseMotionValue
import take.dic.sensorapp.value.motion.MotionValue
import java.util.*
import kotlin.experimental.and

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

    fun getMotionList(since: Long?, status: RealmStatus): List<MotionValue> {
        Realm.getDefaultInstance().use { realm ->
            val list = if (since == null) {
                realm.where(MotionValue::class.java).findAll()
            } else {
                realm.where(MotionValue::class.java).greaterThan("unixTime", since).findAll()
            }
            return list.filter { it.status and status.statusCode != status.statusCode }
        }
    }

    fun convertToMotionObjects(motions: List<MotionValue>): List<MotionObject> {
        val list = LinkedList<MotionObject>()
        motions.forEach { list.add(extractMotionObject(it)) }
        return list
    }

    private fun extractMotionObject(motion: MotionValue): MotionObject = MotionObject(
        Motion(
            motion.accelerationValue!!.x.toString(),
            motion.accelerationValue!!.y.toString(),
            motion.accelerationValue!!.z.toString()
        ),
        Motion(
            motion.directionValue!!.x.toString(),
            motion.directionValue!!.y.toString(),
            motion.directionValue!!.z.toString()
        ),
        Motion(
            motion.gyroValue!!.x.toString(),
            motion.gyroValue!!.y.toString(),
            motion.gyroValue!!.z.toString()
        ),
        motion.unixTime.toString()
    )

    fun getLocationList(since: Long?, status: RealmStatus): List<GPSValue> {
        Realm.getDefaultInstance().use { realm ->
            val list = if (since == null) {
                realm.where(GPSValue::class.java).findAll()
            } else {
                realm.where(GPSValue::class.java).greaterThan("unixTime", since).findAll()
            }
            return list.filter { it.status and status.statusCode != status.statusCode }
        }
    }

    fun convertToLocationObjects(locations: List<GPSValue>): List<LocationObject> {
        val list = LinkedList<LocationObject>()
        locations.forEach { list.add(extractLocationObject(it)) }
        return list
    }

    private fun extractLocationObject(location: GPSValue): LocationObject =
        LocationObject(
            location.latitude.toString(),
            location.longitude.toString(),
            location.altitude.toString(),
            location.unixTime.toString()
        )

    fun getBeaconList(since: Long?, status: RealmStatus): List<BeaconModel> {
        Realm.getDefaultInstance().use { realm ->
            val list = if (since == null) {
                realm.where(BeaconModel::class.java).findAll().sort("receivedTime")
            } else {
                realm.where(BeaconModel::class.java).greaterThan("receivedTime", since).findAll()
                    .sort("receivedTime")
            }
            return list.filter { it.status and status.statusCode != status.statusCode }
        }
    }

    fun convertToBeaconObjects(beaconModel: List<BeaconModel>): List<BeaconObject> {
        val list = LinkedList<BeaconObject>()
        var beacons = beaconModel

        while (true) {
            var count = 1
            val firstBeacon = beacons.firstOrNull() ?: break
            val stBeacons = LinkedList<BeaconModel>()
            val lastIndex = beacons.indexOfFirst { it.receivedTime != firstBeacon.receivedTime }

            for (beacon in beacons) {
                if (lastIndex <= count) {
                    break
                }
                stBeacons.add(beacon)
                count++
            }

            val dropIndex = if (1 < lastIndex) {
                lastIndex
            } else {
                1
            }

            list.add(extractBeaconObject(firstBeacon, stBeacons))
            beacons = beacons.drop(dropIndex)
        }
        return list
    }

    private fun extractBeaconObject(
        beacon: BeaconModel, stBeacons: List<BeaconModel>
    ): BeaconObject {
        val list = LinkedList<Beacon>()
        list.add(extractBeacon(beacon))
        stBeacons.forEach { list.add(extractBeacon(it)) }
        return BeaconObject(list, beacon.receivedTime.toString())
    }

    private fun extractBeacon(beacon: BeaconModel) =
        Beacon(beacon.rssi.toString(), beacon.major, beacon.minor, beacon.distance.toString())
}