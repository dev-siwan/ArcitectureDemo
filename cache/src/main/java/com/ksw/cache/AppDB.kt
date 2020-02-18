package com.ksw.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ksw.cache.AppDB.Companion.DB_VERSION
import com.ksw.cache.common.Converters
import com.ksw.cache.dao.FeedDao
import com.ksw.cache.model.FeedEntity


/*RoomDB 설정*/
@Database(
    entities = [FeedEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDB:RoomDatabase(){

    abstract fun getFeedDao() :FeedDao

    /*싱글턴 패턴*/
    companion object{
        const val DB_VERSION = 1
        private const val DB_NAME ="ArchitectureDemo.db"
        @Volatile
        private var INSTANCE: AppDB? = null

        fun getInstance(context: Context): AppDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }

        private fun build(context: Context) =
            Room.databaseBuilder(
                context.applicationContext, AppDB::class.java,
                DB_NAME
            ).addMigrations(MIGRATION_1_TO_2)
                .build()

        /*마이그레이션*/
        private val MIGRATION_1_TO_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

            }
        }
    }
}