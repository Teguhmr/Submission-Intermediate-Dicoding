package com.dicoding.submissionintermediatedicoding.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediatedicoding.data.story.Story
import com.dicoding.submissionintermediatedicoding.databinding.StoryLayoutBinding
import com.dicoding.submissionintermediatedicoding.ui.detail.DetailActivity
import com.dicoding.submissionintermediatedicoding.utils.Constants
import com.dicoding.submissionintermediatedicoding.utils.TimeStamp

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: StoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .into(storyImage)

                tvStoryName.text = "by ${story.name}"
                tvStoryDesc.text = story.description
                tvTimeline.text = TimeStamp.getTimelineUpload(itemView.context, story.createdAt)

                storyLayoutRoot.setOnClickListener{
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.apply {
                        putExtra(Constants.IMAGE, story.photoUrl)
                        putExtra(Constants.DESC, story.description)
                        putExtra(Constants.NAME, story.name)
                        putExtra(Constants.LAT, story.lat.toString())
                        putExtra(Constants.LNG, story.lon.toString())
                    }

                    val optionsCompat : ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(storyImage, "photo")
                    )

                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  StoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}