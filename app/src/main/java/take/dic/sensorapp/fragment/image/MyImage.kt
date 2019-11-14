package take.dic.sensorapp.fragment.image

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import take.dic.sensorapp.api.model.regular.image.ArrowImg
import take.dic.sensorapp.api.model.regular.image.AvatarImg
import take.dic.sensorapp.api.model.regular.image.BaseImg
import take.dic.sensorapp.service.DeviceInformationManager
import java.io.Serializable

class MyImage(avatarImg: AvatarImg, bottomImg: BaseImg, arrowImg: ArrowImg) : Serializable {
    val avatarImg: ObservableField<AvatarImg> = ObservableField(avatarImg)
    val bottomImg: ObservableField<BaseImg> = ObservableField(bottomImg)
    val arrowImg: ObservableField<ArrowImg> = ObservableField(arrowImg)
}

@BindingAdapter("android:image_bottom")
fun ImageView.loadBottomImage(img: BaseImg) {
    Glide.with(context)
        .asBitmap()
        .load(img.URL)
        .transform(Rotate(img.deg.toInt()))
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val width = if (resource.width < DeviceInformationManager.size.x) {
                    resource.width
                } else {
                    DeviceInformationManager.size.x
                }
                val height = if(resource.height < DeviceInformationManager.size.y) {
                    resource.height
                } else {
                    DeviceInformationManager.size.y
                }

                val bitmap = Bitmap.createBitmap(resource, 0, 0, width, height)
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
        .override(img.IMAGE_SIZE)
        .into(this)
}

@BindingAdapter("android:image_arrow")
fun ImageView.loadDirectionImage(img: ArrowImg) {
    Glide.with(context)
        .load(img.URL)
        .override(img.IMAGE_SIZE)
        .transform(Rotate(img.deg.toInt()))
        .into(this)
}