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
import java.util.*
import kotlin.concurrent.schedule

// TODO: BottomImageの座標調整
class ImageFragment : BaseBindingFragment() {
    var angleCount = 0
    lateinit var timer: Timer
    lateinit var image: MyImage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        image = MyImage(
            AvatarImage("https://images-na.ssl-images-amazon.com/images/I/51XyLHYl4pL._SL1000_.jpg"),
            BottomImage(
                "https://png.pngtree.com/thumb_back/fw800/background/20190223/ourmid/pngtree-blue-night-sky-clouds-stars-quiet-background-backgroundnight-backgroundstarry-backgroundstar-image_85722.jpg",
                0.0,
                listOf(0, 0),
                800.0
            ), DirectionImage(
                "https://yuuyaketoinaho.com/freesozai_image/yazirusi_wakunashi_aka.png",
                0.0
            )
        )
        binding.image = image

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        beginRotate()
    }

    override fun onPause() {
        super.onPause()
        timer.cancel()
    }

    private fun beginRotate() {
        timer = Timer()
        timer.schedule(1000, 1000) {
            image.apply {
                this.avatarImage.set(
                    AvatarImage(
                        this.avatarImage.get()!!.URL
                    )
                )
                this.bottomImage.set(
                    BottomImage(
                        this.bottomImage.get()!!.URL,
                        0.0,
                        this.bottomImage.get()!!.coordinate,
                        this.bottomImage.get()!!.magnification
                    )
                )
                this.directionImage.set(
                    DirectionImage(
                        image.directionImage.get()!!.URL,
                        90.0 * (angleCount++ % 4)
                    )
                )
            }
        }
    }
}