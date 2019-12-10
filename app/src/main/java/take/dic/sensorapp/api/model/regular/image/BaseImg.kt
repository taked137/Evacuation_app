package take.dic.sensorapp.api.model.regular.image

import java.io.Serializable

data class BaseImg(val URL: String, val deg: Double, val offset: Collection<Double>, val exp: Double): Serializable