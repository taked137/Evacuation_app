package take.dic.sensorapp.fragment.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Response
import take.dic.sensorapp.api.controller.ApiController
import take.dic.sensorapp.api.model.regular.RegularResponse
import take.dic.sensorapp.api.model.regular.image.ArrowImg
import take.dic.sensorapp.api.model.regular.image.AvatarImg
import take.dic.sensorapp.api.model.regular.image.BaseImg
import take.dic.sensorapp.databinding.FragmentImageBinding
import take.dic.sensorapp.fragment.value.base.BaseBindingFragment
import java.util.*
import kotlin.concurrent.schedule

// TODO: BottomImageの座標調整
class ImageFragment : BaseBindingFragment() {
    lateinit var timer: Timer
    lateinit var image: MyImage

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentImageBinding.inflate(inflater, container, false)
        image = arguments!!.getSerializable("initialImage") as MyImage
        binding.image = image

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer.schedule(2000, 2000) {
            // 実際に通信して画像を表示する
            ApiController.sendSomeInformation(::setImage)
        }
    }

    override fun onPause() {
        super.onPause()
        if (this::timer.isInitialized) {
            timer.cancel()
        }

        fragmentManager!!.beginTransaction().remove(this).commit()
    }

    private fun setImage(response: Response<RegularResponse>) {
        val body = response.body() ?: return
        image.apply {
            this.avatarImg.set(AvatarImg(body.avatarImg.URL))
            this.bottomImg.set(
                BaseImg(body.baseImg.URL, body.baseImg.deg, body.baseImg.offset, body.baseImg.exp)
            )
            this.arrowImg.set(ArrowImg(body.arrowImg.URL, body.arrowImg.deg))
        }
    }

}