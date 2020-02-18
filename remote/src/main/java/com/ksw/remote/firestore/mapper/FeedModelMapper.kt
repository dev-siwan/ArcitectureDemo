package com.ksw.remote.firestore.mapper

import com.ksw.data.model.FeedData
import com.ksw.remote.firestore.model.FeedModel

class FeedModelMapper  {
    fun mapToModel(type: FeedData): FeedModel {
        type.apply {
            return FeedModel(fId =  fid, title =  title, content =  content, imgUrl =  imgUrl, createDate =  createDate, updateTime =  updateTime)
        }
    }

   fun mapFromModel(type: FeedModel): FeedData {
        type.apply {
            return FeedData(fId!!, title, content, imgUrl, createDate!!, updateTime!!)
        }
    }

}