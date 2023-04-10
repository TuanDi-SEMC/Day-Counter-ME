package com.diagonalley.daycounterme.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.utils.registerEventBus
import com.diagonalley.daycounterme.utils.unregisterEventBus
import com.google.android.material.transition.MaterialFadeThrough
import timber.log.Timber

/**
override fun onAttach(context: Context) {
registerEventBus()
super.onAttach(context)
}

override fun onDetach() {
super.onDetach()
unregisterEventBus()
}
 **/
abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    /*
    * private val args: _FragmentArgs by navArgs()
    * val (position) = args
    * */
    lateinit var binding: T
    open fun isRegisterEventBus(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        Timber.d("#Screen -----------------> %s", this::class.java.simpleName)
    }

    @LayoutRes
    abstract fun getLayoutResId(): Int

    fun showMessage(@StringRes res: Int) {
//        if (activity is MainActivity)
//            (activity as MainActivity).showMessage(res)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        bindingView()
        return binding.root
    }

    open fun bindingView() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (isRegisterEventBus()) {
            registerEventBus()
        }
    }

    override fun onDetach() {
        super.onDetach()
        if (isRegisterEventBus()) {
            unregisterEventBus()
        }
    }
}

