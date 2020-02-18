package com.ksw.presentation.ui.feeds

import androidx.recyclerview.widget.DiffUtil
import com.ksw.domain.model.Feed

class FeedDiffCallback: DiffUtil.ItemCallback<Feed>(){
    override fun areItemsTheSame(oldItem: Feed, newItem: Feed) =
        oldItem.fid == newItem.fid

    override fun areContentsTheSame(oldItem: Feed, newItem: Feed) =
        oldItem == newItem

}