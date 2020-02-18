package com.ksw.cache.mapper

import com.ksw.cache.model.FeedEntity
import com.ksw.data.model.FeedData

class FeedEntityMapper{
     fun mapToEntity(type: FeedData): FeedEntity {
        type.apply {
            return FeedEntity(fid =  fid!!, title =  title, content =  content, imgUrl =  imgUrl, createDate =  createDate, updateTime =  updateTime)
        }
    }

     fun mapFromEntity(type: FeedEntity): FeedData {
        type.apply {
            return FeedData(fid, title, content, imgUrl, createDate, updateTime)
        }
    }

}