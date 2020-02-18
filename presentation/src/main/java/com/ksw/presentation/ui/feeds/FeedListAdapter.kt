package com.ksw.presentation.ui.feeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ksw.domain.model.Feed
import com.ksw.presentation.databinding.ItemFeedBinding

class FeedListAdapter(private val viewModel: FeedsViewModel) :

    PagedListAdapter<Feed, FeedListAdapter.ViewHolder>(FeedDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(viewModel, it)
        }

    }

    class ViewHolder private constructor(private val binding: ItemFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: FeedsViewModel, feed: Feed) {
            binding.viewModel = viewModel
            binding.feed = feed
            binding.executePendingBindings()
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFeedBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }


}