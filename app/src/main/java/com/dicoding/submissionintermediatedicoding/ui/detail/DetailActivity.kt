package com.dicoding.submissionintermediatedicoding.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediatedicoding.databinding.ActivityDetailBinding
import com.dicoding.submissionintermediatedicoding.utils.bottomsheet.BottomSheetDialogDetail

class DetailActivity : AppCompatActivity() {
    private var detailBinding: ActivityDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding!!.root)
        supportActionBar?.hide()
        initView()
        showDetail()
    }

    private fun initView(){
        detailBinding?.fabShowInfo?.setOnClickListener {
            BottomSheetDialogDetail().show(supportFragmentManager, BottomSheetDialogDetail.TAG)
        }

    }

    private fun showDetail(){
        val img = intent.getStringExtra("IMAGE")

        detailBinding?.apply {
            Glide.with(this@DetailActivity)
                .load(img)
                .into(storyImage)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailBinding = null
    }
}