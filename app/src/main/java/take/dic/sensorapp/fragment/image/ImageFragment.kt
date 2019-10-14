package take.dic.sensorapp.fragment.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Response
import take.dic.sensorapp.api.model.regular.RegularResponse
import take.dic.sensorapp.api.model.regular.image.AvatarImage
import take.dic.sensorapp.api.model.regular.image.BottomImage
import take.dic.sensorapp.api.model.regular.image.DirectionImage
import take.dic.sensorapp.databinding.FragmentImageBinding
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment
import java.util.*
import kotlin.concurrent.schedule

// TODO: BottomImageの座標調整
class ImageFragment : BaseBindingFragment() {
    private val moveMap = linkedMapOf(30 to 180, 100 to 300, 250 to 500, 101 to 300)
    private lateinit var timer: Timer
    private lateinit var moveTimer: Timer
    private lateinit var image: MyImage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        image = MyImage(
            AvatarImage("https://images-na.ssl-images-amazon.com/images/I/51XyLHYl4pL._SL1000_.jpg"),
            BottomImage(
                "https://png.pngtree.com/thumb_back/fw800/background/20190223/ourmid/pngtree-blue-night-sky-clouds-stars-quiet-background-backgroundnight-backgroundstarry-backgroundstar-image_85722.jpg",
                0.0,
                listOf(moveMap.entries.elementAt(0).key, moveMap.entries.elementAt(0).value),
                800.0
            ),
            DirectionImage(
                "https://yuuyaketoinaho.com/freesozai_image/yazirusi_wakunashi_aka.png",
                0.0
            )
        )
        binding.image = image
        binding.fragment = this

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        if(this::timer.isInitialized) {
            timer.cancel()
        }
        if(this::moveTimer.isInitialized){
            moveTimer.cancel()
        }
    }

    private fun setImage(response: Response<RegularResponse>) {
        val body = response.body() ?: return
        image.apply {
            this.avatarImage.set(
                AvatarImage(
                    body.avatarImage.URL
                )
            )
            this.bottomImage.set(
                BottomImage(
                    body.bottomImage.URL,
                    body.bottomImage.angle,
                    body.bottomImage.coordinate,
                    body.bottomImage.magnification
                )
            )
            this.directionImage.set(
                DirectionImage(
                    body.directionImage.URL,
                    body.directionImage.angle
                )
            )
        }
    }

    fun startRotate(view: View){
        var angleCount = 0
        if(this::timer.isInitialized){
            timer.cancel()
        }

        timer = Timer().apply {
            this.schedule(0, 2000) {
                rotateImage(++angleCount)
            }
        }
    }

    private fun rotateImage(angleCount: Int) {
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
                    90.0 * (angleCount % 4)
                )
            )
        }
    }

    fun startMove(view: View){
        var count = 0
        if(this::moveTimer.isInitialized){
            moveTimer.cancel()
        }

        moveTimer = Timer().apply {
            this.schedule(0, 2000) {
                moveImage(++count)
            }
        }
    }

    private fun moveImage(count: Int) {
        val entry = moveMap.entries.elementAt(count % 4)
        image.apply {
            this.avatarImage.set(
                AvatarImage(
                    this.avatarImage.get()!!.URL
                )
            )
            this.bottomImage.set(
                BottomImage(
                    this.bottomImage.get()!!.URL,
                    this.bottomImage.get()!!.angle,
                    listOf(entry.key, entry.value),
                    this.bottomImage.get()!!.magnification
                )
            )
            this.directionImage.set(
                DirectionImage(
                    image.directionImage.get()!!.URL,
                    image.directionImage.get()!!.angle
                )
            )
        }
    }
}