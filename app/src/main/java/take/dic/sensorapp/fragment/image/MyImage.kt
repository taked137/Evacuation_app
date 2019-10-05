package take.dic.sensorapp.fragment.image

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import take.dic.sensorapp.api.model.regular.image.AvatarImage
import take.dic.sensorapp.api.model.regular.image.BottomImage
import take.dic.sensorapp.api.model.regular.image.DirectionImage

data class MyImage(
    val avatarImage: AvatarImage,
    val bottomImage: BottomImage,
    val directionImage: DirectionImage
)

@BindingAdapter("android:image_bottom")
fun ImageView.loadBottomImage(image: BottomImage) {
    Glide.with(context)
        .load(image.URL)
        .override(image.magnification.toInt())
        .transform(Rotate(image.angle.toInt()))
        .into(this)
}

@BindingAdapter("android:image_avatar")
fun ImageView.loadAvatarImage(image: AvatarImage) {
    Glide.with(context)
        .load(image.URL)
        .override(500)
        .into(this)
}

@BindingAdapter("android:image_direction")
fun ImageView.loadDirectionImage(image: DirectionImage) {
    Glide.with(context)
        .load(image.URL)
        .override(300)
        .transform(Rotate(image.angle.toInt()))
        .into(this)
}

@BindingAdapter("android:layout_margin")
fun setMargin(view: View, coordinate: List<Int>) {
    view.layoutParams = (view.layoutParams as MarginLayoutParams).apply {
        this.rightMargin = coordinate[0]
        this.topMargin = coordinate[1]
    }
}