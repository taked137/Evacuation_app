package take.dic.sensorapp.service

import io.realm.Realm
import take.dic.sensorapp.api.model.*
import take.dic.sensorapp.sensorvalue.GPSValue
import take.dic.sensorapp.sensorvalue.beacon.BeaconModel
import take.dic.sensorapp.sensorvalue.motion.BaseMotionValue
import take.dic.sensorapp.sensorvalue.motion.MotionValue
import java.util.*
import kotlin.experimental.and

// TODO: 都度送信、一括送信後のデータの扱い(削除?, 一応保存?)
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

    fun getMotionList(since: Long?, status: RealmStatus): Collection<MotionValue> {
        Realm.getDefaultInstance().use { realm ->
            val list = if (since == null) {
                realm.where(MotionValue::class.java).findAll()
            } else {
                realm.where(MotionValue::class.java).greaterThan("unixTime", since).findAll()
            }
            return realm.copyFromRealm(list).filter { it.status and status.statusCode != status.statusCode }
        }
    }

    fun convertToMotionObjects(motions: Collection<MotionValue>): Collection<MotionObject> {
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

    fun getLocationList(since: Long?, status: RealmStatus): Collection<GPSValue> {
        Realm.getDefaultInstance().use { realm ->
            val list = if (since == null) {
                realm.where(GPSValue::class.java).findAll()
            } else {
                realm.where(GPSValue::class.java).greaterThan("unixTime", since).findAll()
            }
            return realm.copyFromRealm(list).filter { it.status and status.statusCode != status.statusCode }
        }
    }

    fun convertToLocationObjects(locations: Collection<GPSValue>): Pair<Collection<LocationObject>, Collection<HeadingObject>> {
        val locationList = LinkedList<LocationObject>()
        val headingList = LinkedList<HeadingObject>()
        for(location in locations) {
            locationList.add(extractLocationObject(location))
            headingList.add(extractHeadingObject(location))
        }

        return Pair(locationList, headingList)
    }

    private fun extractLocationObject(location: GPSValue): LocationObject =
        LocationObject(
            location.latitude.toString(),
            location.longitude.toString(),
            location.altitude.toString(),
            location.unixTime.toString()
        )

    private fun extractHeadingObject(location: GPSValue): HeadingObject =
        HeadingObject(
            location.direction.toString(),
            location.unixTime.toString()
        )

    fun getBeaconList(since: Long?, status: RealmStatus): Collection<BeaconModel> {
        Realm.getDefaultInstance().use { realm ->
            val list = if (since == null) {
                realm.where(BeaconModel::class.java).findAll().sort("receivedTime")
            } else {
                realm.where(BeaconModel::class.java).greaterThan("receivedTime", since).findAll()
                    .sort("receivedTime")
            }
            return realm.copyFromRealm(list).filter { it.status and status.statusCode != status.statusCode }
        }
    }

    fun convertToBeaconObjects(beaconModel: Collection<BeaconModel>): Collection<BeaconObject> {
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

    private fun extractBeaconObject(beacon: BeaconModel, stBeacons: Collection<BeaconModel>): BeaconObject {
        val list = LinkedList<Beacon>()
        list.add(extractBeacon(beacon))
        stBeacons.forEach { list.add(extractBeacon(it)) }
        return BeaconObject(list, beacon.receivedTime.toString())
    }

    private fun extractBeacon(beacon: BeaconModel) =
        Beacon(beacon.rssi.toString(), beacon.major, beacon.minor, beacon.distance.toString())
}