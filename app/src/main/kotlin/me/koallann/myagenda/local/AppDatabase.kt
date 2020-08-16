package me.koallann.myagenda.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.koallann.myagenda.domain.Topic
import me.koallann.myagenda.local.topic.TopicDao
import me.koallann.myagenda.local.topic.TopicEntity
import me.koallann.myagenda.local.topic.TopicTypeConverter
import me.koallann.myagenda.local.user.UserDao
import me.koallann.myagenda.local.user.UserEntity

@Database(version = 1, entities = [UserEntity::class, TopicEntity::class], exportSchema = false)
@TypeConverters(TopicTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "myagenda.db"
            )
                .build()
        }
    }

    abstract fun getUserDao(): UserDao

    abstract fun getTopicDao(): TopicDao

}
