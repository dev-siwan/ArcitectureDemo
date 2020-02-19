package com.ksw.cache.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ksw.cache.BaseDao
import com.ksw.cache.model.FeedEntity
import java.util.*

/** Feed Dao **/
@Dao
interface FeedDao : BaseDao<FeedEntity> {

    @Query("SELECT * FROM FeedEntity ORDER BY updateTime DESC")
    override fun selectAllPaged(): DataSource.Factory<Int, FeedEntity>

    @Query("SELECT * FROM FeedEntity WHERE fId = :id")
    override fun select(id: String): FeedEntity

    @Transaction
    fun replaceList(dataList: List<FeedEntity>) {

        if(dataList.isNotEmpty()) {

            val minUpdateTime=
                dataList.last().updateTime


            val maxUpdateTime =
                dataList.first().updateTime


            deleteRange(minUpdateTime, maxUpdateTime)
            insertList(dataList = dataList)
        }
    }

    @Query("DELETE FROM FeedEntity WHERE updateTime BETWEEN :minDate AND :maxDate")
    fun deleteRange(minDate: Long, maxDate: Long)
}