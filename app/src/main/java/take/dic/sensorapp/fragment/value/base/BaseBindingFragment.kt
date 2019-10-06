package take.dic.sensorapp.fragment.value.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class BaseBindingFragment : Fragment() {
    lateinit var binding: ViewDataBinding

    inline fun <reified T : ViewDataBinding> bind(
        inflater: LayoutInflater, container: ViewGroup?, layoutId: Int
    ): T {
        binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false)
        return binding as T
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::binding.isInitialized) {
            binding.unbind()
        }
    }
}