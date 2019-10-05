package take.dic.sensorapp.fragment.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import take.dic.sensorapp.api.model.regular.image.AvatarImage
import take.dic.sensorapp.api.model.regular.image.BottomImage
import take.dic.sensorapp.api.model.regular.image.DirectionImage
import take.dic.sensorapp.databinding.FragmentImageBinding
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment

// TODO: BottomImageの座標調整
class ImageFragment : BaseBindingFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        binding.image = MyImage(
            AvatarImage("https://images-na.ssl-images-amazon.com/images/I/51XyLHYl4pL._SL1000_.jpg"),
            BottomImage(
                "https://png.pngtree.com/thumb_back/fw800/background/20190223/ourmid/pngtree-blue-night-sky-clouds-stars-quiet-background-backgroundnight-backgroundstarry-backgroundstar-image_85722.jpg",
                0.0,
                listOf(0, 0),
                800.0
            ),
            DirectionImage(
                "https://yuuyaketoinaho.com/freesozai_image/yazirusi_wakunashi_aka.png",
                0.0
            )
        )

        return binding.root
    }
}