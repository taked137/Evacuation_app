package take.dic.sensorapp.fragment.image

import android.databinding.BindingAdapter
import android.databinding.ObservableField
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import take.dic.sensorapp.api.model.regular.image.AvatarImage
import take.dic.sensorapp.api.model.regular.image.BottomImage
import take.dic.sensorapp.api.model.regular.image.DirectionImage

class MyImage(avatarImage: AvatarImage, bottomImage: BottomImage, directionImage: DirectionImage) {
    val avatarImage: ObservableField<AvatarImage> = ObservableField(avatarImage)
    val bottomImage: ObservableField<BottomImage> = ObservableField(bottomImage)
    val directionImage: ObservableField<DirectionImage> = ObservableField(directionImage)
}

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
        this.leftMargin = coordinate[0]
        this.topMargin = coordinate[1]
    }
}