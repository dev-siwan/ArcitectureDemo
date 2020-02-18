package com.ksw.data.mapper

import com.ksw.data.model.FeedData
import com.ksw.domain.model.Feed

class FeedDataMapper {
     fun mapToData(type: Feed): FeedData {
        type.apply {
            return FeedData(fid =fid, title =  title, content =  content, imgUrl =  imgUrl, createDate =  createDate, updateTime =  updateTime)
        }
    }
     fun mapFromData(type: FeedData): Feed {
        type.apply {
            return Feed(fid =fid, title =  title, content =  content, imgUrl =  imgUrl, createDate =  createDate, updateTime =  updateTime)
        }
    }

}