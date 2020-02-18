package com.ksw.domain.model

import java.util.*


data class Feed(
    var fid: String?=null,
    val title: String,
    val content: String,
    var imgUrl: String?=null,
    val createDate:Long,
    val updateTime:Long
)
