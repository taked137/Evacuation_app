package take.dic.sensorapp.api.model.regular

import take.dic.sensorapp.api.model.regular.image.AvatarImg
import take.dic.sensorapp.api.model.regular.image.BaseImg
import take.dic.sensorapp.api.model.regular.image.ArrowImg

data class RegularResponse(
    val baseImg: BaseImg,
    val arrowImg: ArrowImg,
    val avatarImg: AvatarImg
)