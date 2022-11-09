package com.dicoding.submissionintermediatedicoding.utils.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dicoding.submissionintermediatedicoding.databinding.BottomSheetDetailLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialogDetail: BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetDetailLayoutBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = activity?.intent?.getStringExtra("NAME")
        val desc = activity?.intent?.getStringExtra("DESCRIPTION")

        binding.apply {
            btnClose.setOnClickListener {
                dismiss()
            }
            textDetailDescription.text = desc
            textName.text = "by : $name"
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