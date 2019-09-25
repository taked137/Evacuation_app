package take.dic.sensorapp.api.controller

import io.realm.Realm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import take.dic.sensorapp.api.model.Device
import take.dic.sensorapp.api.model.Payload
import take.dic.sensorapp.api.model.all.AllRequest
import take.dic.sensorapp.api.model.all.AllResponse
import take.dic.sensorapp.api.service.APIClient
import take.dic.sensorapp.service.RealmManager
import take.dic.sensorapp.service.RealmStatus
import take.dic.sensorapp.value.GPSValue
import take.dic.sensorapp.value.beacon.BeaconModel
import take.dic.sensorapp.value.motion.MotionValue
import kotlin.experimental.or

class ApiController {
    // Realm内の情報を一括送信するメソッド
    fun sendAllInformation(since: Long?, onResponse: (response: Response<AllResponse>) -> Unit) {
        val motionList = RealmManager.getMotionList(since)
        val locationList = RealmManager.getLocationList(since)
        val beaconList = RealmManager.getBeaconList(since)
        val request = AllRequest(
            since = since?.toString(),
            device = Device(),
            payload = Payload(
                RealmManager.convertToMotionObjects(motionList),
                listOf(),
                RealmManager.convertToLocationObjects(locationList),
                RealmManager.convertToBeaconObjects(beaconList)
            )
        )
        APIClient.instance.all(request).enqueue(object : Callback<AllResponse> {
            // 送信成功時
            override fun onResponse(call: Call<AllResponse>, response: Response<AllResponse>) {
                onResponse(response)
                updateStatus(motionList, locationList, beaconList)
            }

            // 送信失敗時
            override fun onFailure(call: Call<AllResponse>, t: Throwable) {

            }
        })
    }

    private fun updateStatus(motionList: List<MotionValue>, locationList: List<GPSValue>, beaconList: List<BeaconModel>){
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                for (motion in motionList) {
                    val motionModel = realm.where(MotionValue::class.java).equalTo(
                        "id", motion.id
                    ).findFirst() ?: continue
                    motionModel.status =
                        motionModel.status or RealmStatus.ALL_SENT.statusCode
                }
                for (location in locationList) {
                    val locationModel = realm.where(GPSValue::class.java).equalTo(
                        "id", location.id
                    ).findFirst() ?: continue
                    locationModel.status =
                        locationModel.status or RealmStatus.ALL_SENT.statusCode
                }
                for (beacon in beaconList) {
                    val beaconModel = realm.where(BeaconModel::class.java).equalTo(
                        "id", beacon.id
                    ).findFirst() ?: continue
                    beaconModel.status =
                        beaconModel.status or RealmStatus.ALL_SENT.statusCode
                }
            }
        }
    }
}