package com.ksw.cache.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**룸 피드 데이터 모델**/
@Entity
data class FeedEntity(
    @PrimaryKey(autoGenerate = false)
    var fid: String,
    val title: String,
    val content: String,
    var imgUrl: String?=null,
    val createDate:Long,
    val updateTime:Long
)
