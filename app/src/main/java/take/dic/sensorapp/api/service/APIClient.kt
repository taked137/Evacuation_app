package take.dic.sensorapp.api.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class APIClient {
    companion object {
        private const val BASE_URL = "https://exit.ske.nitech.ac.jp/api/v1/"

        val instance: APIService by lazy {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            retrofit.create(APIService::class.java)
        }
    }
}