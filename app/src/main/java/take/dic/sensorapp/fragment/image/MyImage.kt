package take.dic.sensorapp.fragment.image

import android.databinding.BindingAdapter
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.Rotate
import kotlin.math.roundToInt

data class MyImage(val imageInfo: ImageInfo, val marginInfo: MarginInfo)
data class ImageInfo(val imageUrl: String, val size: Int, val rotateDegree: Int)
data class MarginInfo(
    val topMargin: Float?,
    val rightMargin: Float?,
    val bottomMargin: Float?,
    val leftMargin: Float?
)

@BindingAdapter("android:imageUrl")
fun ImageView.loadImage(imageInfo: ImageInfo) {
    Glide.with(context).load(imageInfo.imageUrl).override(imageInfo.size)
        .transform(Rotate(imageInfo.rotateDegree)).into(this)
}

@BindingAdapter("layout_marginTop")
fun setMargin(view: View, marginInfo: MarginInfo) {
    view.layoutParams = (view.layoutParams as MarginLayoutParams).apply {
        marginInfo.topMargin?.let { this.topMargin = it.roundToInt() }
        marginInfo.rightMargin?.let { this.rightMargin = it.roundToInt() }
        marginInfo.bottomMargin?.let { this.bottomMargin = it.roundToInt() }
        marginInfo.leftMargin?.let { this.leftMargin = it.roundToInt() }
    }
}