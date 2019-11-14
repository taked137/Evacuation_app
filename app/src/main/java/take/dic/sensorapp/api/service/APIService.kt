package take.dic.sensorapp.api.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import take.dic.sensorapp.api.model.all.AllRequest
import take.dic.sensorapp.api.model.regular.RegularRequest
import take.dic.sensorapp.api.model.regular.RegularResponse

interface APIService {
    @POST("bulk")
    fun all(@Body request: AllRequest): Call<Void>

    @POST("guideImmediately")
    fun regular(@Body request: RegularRequest): Call<RegularResponse>
}