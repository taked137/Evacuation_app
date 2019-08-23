package take.dic.sensorapp.image

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

data class MyImage(val imageUrl: String)
@BindingAdapter("android:imageUrl")
fun ImageView.loadImage(imageUrl: String){
    Glide.with(context).load(imageUrl).into(this)
}