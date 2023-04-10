package com.diagonalley.daycounterme.ui.sheet

import android.os.Bundle
import android.view.View
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseBottomSheet
import com.diagonalley.daycounterme.databinding.PickImageBottomSheetBinding
import com.diagonalley.daycounterme.utils.setSingleClick


class PickImageBottomSheet private constructor() :
    BaseBottomSheet<PickImageBottomSheetBinding>(R.layout.pick_image_bottom_sheet) {
    companion object {
        internal const val TAG = "PickImageBottomSheet"
        fun newInstance(): PickImageBottomSheet {
            val args = Bundle()
            val fragment = PickImageBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    var listener: OnPickImageListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnTakePhoto.setSingleClick {
                dismiss()
                listener?.takePhoto()
            }
            btnGallery.setSingleClick {
                dismiss()
                listener?.openGallery()
            }
        }
    }
}

interface OnPickImageListener {
    fun takePhoto()
    fun openGallery()
}