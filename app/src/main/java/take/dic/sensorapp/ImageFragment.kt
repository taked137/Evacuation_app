package take.dic.sensorapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide

class ImageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        Glide.with(this)
            .load("https://40.media.tumblr.com/f49e56a443aecd533fb53d55a1cf1408/tumblr_nsc4fht5ol1u3hv5ko1_1280.jpg")
            .into(imageView)
        return view
    }

}