package take.dic.sensorapp.image

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        binding.image = MyImage("https://40.media.tumblr.com/f49e56a443aecd533fb53d55a1cf1408/tumblr_nsc4fht5ol1u3hv5ko1_1280.jpg")

        return binding.root
    }
}