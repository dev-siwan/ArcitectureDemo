package com.ksw.remote.firestore.model

data class FeedModel(var fId: String?=null,
                     val title: String="",
                     val content: String="",
                     var imgUrl: String?=null,
                     val createDate:Long?=null,
                     val updateTime:Long?=null)