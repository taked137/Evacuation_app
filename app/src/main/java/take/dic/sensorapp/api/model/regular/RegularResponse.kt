package take.dic.sensorapp.api.model.regular

import take.dic.sensorapp.api.model.regular.image.AvatarImage
import take.dic.sensorapp.api.model.regular.image.BottomImage
import take.dic.sensorapp.api.model.regular.image.DirectionImage

// TODO: 命名
data class RegularResponse(
    val bottomImage: BottomImage,
    val directionImage: DirectionImage,
    val avatarImage: AvatarImage
)