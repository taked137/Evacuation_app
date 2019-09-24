package take.dic.sensorapp.api.controller

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import take.dic.sensorapp.api.model.Device
import take.dic.sensorapp.api.model.Payload
import take.dic.sensorapp.api.model.all.AllRequest
import take.dic.sensorapp.api.model.all.AllResponse
import take.dic.sensorapp.api.service.APIClient
import take.dic.sensorapp.service.RealmManager

class ApiController {
    // Realm内の全情報を一括送信するメソッド
    fun sendAllInformation(onResponse: (response: Response<AllResponse>) -> Unit) {
        val request = AllRequest(
            device = Device(),
            payload = Payload(
                RealmManager.getMotionObjects(),
                listOf(),
                RealmManager.getLocationObjects(),
                RealmManager.getBeaconObjects()
            )
        )
        APIClient.instance.all(request).enqueue(object : Callback<AllResponse> {
            // 送信成功時
            override fun onResponse(call: Call<AllResponse>, response: Response<AllResponse>) {
                onResponse(response)
            }
            // 送信失敗時
            override fun onFailure(call: Call<AllResponse>, t: Throwable) {

            }
        })
    }
}