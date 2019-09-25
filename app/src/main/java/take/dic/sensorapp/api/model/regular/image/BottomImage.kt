package take.dic.sensorapp.api.model.regular.image

data class BottomImage(
    val URL: String,
    val angle: Double,
    val coordinate: List<Int>,
    val magnification: Double
)