package com.dicoding.submissionintermediatedicoding.utils.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.dicoding.submissionintermediatedicoding.databinding.BottomSheetDetailLayoutBinding
import com.dicoding.submissionintermediatedicoding.utils.Constants
import com.dicoding.submissionintermediatedicoding.utils.LocationMaps
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogDetail: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDetailLayoutBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = activity?.intent?.getStringExtra(Constants.NAME)
        val desc = activity?.intent?.getStringExtra(Constants.DESC)
        val lat = activity?.intent?.getStringExtra(Constants.LAT)
        val lng = activity?.intent?.getStringExtra(Constants.LNG)

        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
            textDetailDescription.text = desc
            textName.text = "by : $name"
            try {
                textLocation.text =
                    LocationMaps.parseAddressLocation(requireActivity(), lat?.toDouble() ?: 0.0,
                        lng?.toDouble() ?: 0.0
                    )
                binding.textLocation.isVisible = true
            } catch (e: Exception) {
                binding.textLocation.isVisible = false
            }

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetDetailLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        const val TAG = "BottomSheetDetail"
    }

}