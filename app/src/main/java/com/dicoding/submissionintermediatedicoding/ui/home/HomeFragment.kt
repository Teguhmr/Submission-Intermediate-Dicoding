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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dicoding.submissionintermediatedicoding.MainActivity
import com.dicoding.submissionintermediatedicoding.adapter.StoryAdapter
import com.dicoding.submissionintermediatedicoding.adapter.StoryLoadingStateAdapter
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences
import com.dicoding.submissionintermediatedicoding.databinding.FragmentHomeBinding
import com.dicoding.submissionintermediatedicoding.ui.map.MapsActivity
import com.dicoding.submissionintermediatedicoding.ui.story.UploadStoryActivity
import com.dicoding.submissionintermediatedicoding.utils.EspressoIdlingResource
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : Fragment() {
    private  var homeBinding: FragmentHomeBinding? = null
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

        homeBinding!!.rvStory.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter =
                storyAdapter.withLoadStateFooter(
                    footer = StoryLoadingStateAdapter { storyAdapter.retry() })
        }
        homeBinding!!.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }
    }

    private fun initViewModel() {
        val mainViewModel = (activity as MainActivity).getStoryViewModel()

        mainViewModel.story().observe(viewLifecycleOwner) {
            storyAdapter.submitData(
                lifecycle,
                it
            )

        }

        lifecycleScope.launch {
            //Your adapter's loadStateFlow here
            storyAdapter.loadStateFlow.
            distinctUntilChangedBy {
                it.refresh
            }.filter {
                it.refresh is LoadState.NotLoading
            }.collect {
                //you get all the data here
                val list = storyAdapter.snapshot()
                for (i in list){
                    if (i != null) {
                        Log.e("Stack", i.photoUrl)
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
        }


        homeBinding!!.apply {
            storyAdapter.addLoadStateListener { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    EspressoIdlingResource.increment()
                    shimmerEffect.progressBar.visibility = View.VISIBLE
                    rvStory.visibility = View.GONE
                } else {
                    EspressoIdlingResource.decrement()
                    shimmerEffect.progressBar.visibility = View.GONE
                    rvStory.visibility = View.VISIBLE
                    // If we have an error
                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Log.e("Home Fragment", it.error.message.toString())
                    }
                }
            }

            if (rvStory.isVisible){
                scrollToPosition()
            }
        }

    }

    private fun onRefresh() {
        homeBinding!!.apply {
            storyAdapter.refresh()
            Timer().schedule(2000) {
                swipeRefresh.isRefreshing = false
                rvStory.smoothScrollToPosition(0)
            }
        }
    }

    fun scrollToPosition(){
        homeBinding!!.rvStory.smoothScrollToPosition(0)
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

            fabToMap.setOnClickListener {
                val intent = Intent(requireActivity(), MapsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        homeBinding = null
    }

}