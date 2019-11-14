package take.dic.sensorapp.api.model.regular.image

import java.io.Serializable

data class BaseImg(val URL: String, val deg: Double, val offset: List<Int>, val exp: Double): Serializable