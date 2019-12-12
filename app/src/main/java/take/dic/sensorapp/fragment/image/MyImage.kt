package take.dic.sensorapp.fragment.image

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.ViewGroup
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
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

class MyImage(avatarImg: AvatarImg, baseImg: BaseImg, arrowImg: ArrowImg) : Serializable {
    val avatarImg: ObservableField<AvatarImg> = ObservableField(avatarImg)
    val baseImg: ObservableField<BaseImg> = ObservableField(baseImg)
    val arrowImg: ObservableField<ArrowImg> = ObservableField(arrowImg)
}

@BindingAdapter("android:image_bottom")
fun ImageView.loadBottomImage(img: BaseImg) {
    val newOffset = LinkedList<Pair<Boolean, Int>>()
    img.offset.forEach { newOffset.add(Pair(it < 0, abs(it.roundToInt()))) }

    Glide.with(context)
        .asBitmap()
        .load(img.URL)
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .skipMemoryCache(true)
        .transform(Rotate(img.deg.toInt()))
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val width =
                    if (DeviceInformationManager.size.x + newOffset[0].second < resource.width) {
                        DeviceInformationManager.size.x / img.exp
                    } else {
                        (resource.width - newOffset[0].second) / img.exp
                    }

                val height =
                    if (DeviceInformationManager.size.y + newOffset[1].second < resource.height) {
                        DeviceInformationManager.size.y / img.exp
                    } else {
                        (resource.height - newOffset[1].second) / img.exp
                    }

                val xMargin = if (newOffset[0].first) {
                    0
                } else {
                    newOffset[0].second
                }
                val yMargin = if (newOffset[1].first) {
                    0
                } else {
                    newOffset[1].second
                }

                val bitmap = Bitmap.createBitmap(resource, xMargin, yMargin, width.roundToInt(), height.roundToInt())
                Glide.with(context).load(bitmap!!).into(this@loadBottomImage)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
    setMargin(newOffset)
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

fun ImageView.setMargin(offset: List<Pair<Boolean, Int>>) {
    this.layoutParams = (this.layoutParams as ViewGroup.MarginLayoutParams).apply {
        this.leftMargin = if (offset[0].first) {
            offset[0].second
        } else {
            0
        }

        this.topMargin = if (offset[1].first) {
            offset[1].second
        }
        else {
            0
        }
    }
}