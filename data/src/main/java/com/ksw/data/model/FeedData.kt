package com.ksw.data.model

import java.util.*

data class FeedData(
    var fid: String?=null,
    val title: String,
    val content: String,
    var imgUrl: String?=null,
    val createDate:Long,
    val updateTime:Long
)
