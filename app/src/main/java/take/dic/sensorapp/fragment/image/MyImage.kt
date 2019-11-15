package take.dic.sensorapp.fragment.image

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import take.dic.sensorapp.api.model.regular.image.ArrowImg
import take.dic.sensorapp.api.model.regular.image.AvatarImg
import take.dic.sensorapp.api.model.regular.image.BaseImg
import take.dic.sensorapp.service.DeviceInformationManager
import java.io.Serializable
import kotlin.math.roundToInt

class MyImage(avatarImg: AvatarImg, baseImg: BaseImg, arrowImg: ArrowImg) : Serializable {
    val avatarImg: ObservableField<AvatarImg> = ObservableField(avatarImg)
    val baseImg: ObservableField<BaseImg> = ObservableField(baseImg)
    val arrowImg: ObservableField<ArrowImg> = ObservableField(arrowImg)
}

@BindingAdapter("android:image_bottom")
fun ImageView.loadBottomImage(img: BaseImg) {
    Glide.with(context)
        .asBitmap()
        .load(img.URL)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .transform(Rotate(img.deg.toInt()))
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val width =
                    if (DeviceInformationManager.size.x + img.offset.elementAt(0) < resource.width) {
                        DeviceInformationManager.size.x / img.exp
                    } else {
                        (resource.width - img.offset.elementAt(0)) / img.exp
                    }

                val height =
                    if (DeviceInformationManager.size.y + img.offset.elementAt(1) < resource.height) {
                        DeviceInformationManager.size.y / img.exp
                    } else {
                        (resource.height - img.offset.elementAt(1)) / img.exp
                    }

                val bitmap = Bitmap.createBitmap(resource, img.offset.
                    elementAt(0), img.offset.elementAt(1), width.roundToInt(), height.roundToInt())
                Glide.with(context).load(bitmap).into(this@loadBottomImage)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}

@BindingAdapter("android:image_avatar")
fun ImageView.loadAvatarImage(img: AvatarImg) {
    Glide.with(context)
        .load(img.URL)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .override(img.IMAGE_SIZE)
        .into(this)
}

@BindingAdapter("android:image_arrow")
fun ImageView.loadDirectionImage(img: ArrowImg) {
    Glide.with(context)
        .load(img.URL)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .override(img.IMAGE_SIZE)
        .transform(Rotate(img.deg.toInt()))
        .into(this)
}