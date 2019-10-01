package take.dic.sensorapp.fragment.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.databinding.FragmentImageBinding
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment

class ImageFragment : BaseBindingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        binding.image = MyImage(
            ImageInfo(
                "https://40.media.tumblr.com/f49e56a443aecd533fb53d55a1cf1408/tumblr_nsc4fht5ol1u3hv5ko1_1280.jpg",
                500,
                90
            ),
            MarginInfo(200f, null, null, null)
        )

        return binding.root
    }
}