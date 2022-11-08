package com.dicoding.submissionintermediatedicoding.ui.home

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.submissionintermediatedicoding.MainActivity
import com.dicoding.submissionintermediatedicoding.adapter.StoryAdapter
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.databinding.FragmentHomeBinding
import com.dicoding.submissionintermediatedicoding.ui.story.UploadStoryActivity
import com.dicoding.submissionintermediatedicoding.utils.Status
import com.dicoding.submissionintermediatedicoding.viewmodel.StoriesViewModel
import java.io.ByteArrayOutputStream

class HomeFragment : Fragment() {
    private  var homeBinding: FragmentHomeBinding? = null
    private lateinit var storyViewModel: StoriesViewModel
    private val widgetItems = ArrayList<String>()
    private lateinit var usrLoginPref: UserLoginPreferences
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)

        return homeBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storyAdapter = StoryAdapter()
        usrLoginPref = UserLoginPreferences(requireContext())
        initViewModel()
        (activity as MainActivity).supportActionBar?.apply {
            title = "Dicoding Stories"
            show()
        }

        initView()
    }

    private fun initViewModel() {
        storyViewModel = ViewModelProvider(this)[StoriesViewModel::class.java]
        storyViewModel.apply {
            homeBinding?.apply {
                getAllStoriesData((activity as MainActivity).userLoginPref.getLoginData().token)
                listStoryData.observe(requireActivity()) {
                    when(it.status) {
                        Status.LOADING -> {
                            shimmerEffect.progressBar.visibility = View.VISIBLE
                            imageError.visibility = View.GONE
                        }
                        Status.SUCCESS -> {
                            shimmerEffect.progressBar.visibility = View.GONE
                            imageError.visibility = View.GONE
                            if (it.data != null) {
                                storyAdapter.setStoryData(it.data)
                                for (i in it.data){
                                    println(Log.e("Stack ", i.photoUrl))
                                    Glide.with(requireActivity())
                                        .asBitmap()
                                        .load(i.photoUrl)
                                        .into(object : CustomTarget<Bitmap>(){
                                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                widgetItems.addAll(listOf(encodeImage(resource)!!))
                                                usrLoginPref.storeDataArrayList(widgetItems)

                                            }
                                            override fun onLoadCleared(placeholder: Drawable?) {
                                                // this is called when imageView is cleared on lifecycle call or for
                                                // some other reason.
                                                // if you are referencing the bitmap somewhere else too other than this imageView
                                                // clear it here as you can no longer have the bitmap
                                            }
                                        })
                                }
                            }

                        }
                        Status.ERROR -> {
                            shimmerEffect.progressBar.visibility = View.GONE
                            imageError.visibility = View.VISIBLE
                        }
                        Status.EMPTY -> {
                            shimmerEffect.progressBar.visibility = View.GONE
                            imageError.visibility = View.VISIBLE
                        }
                    }
                }
            }


        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun initView() {

        homeBinding?.apply {
            rvStory.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                adapter = storyAdapter
            }

            fabAddStory.setOnClickListener {

                val intent = Intent(requireActivity(), UploadStoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        homeBinding = null
    }

}