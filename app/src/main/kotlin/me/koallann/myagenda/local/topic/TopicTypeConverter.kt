package me.koallann.myagenda.local.topic

import androidx.room.TypeConverter
import me.koallann.myagenda.domain.Topic

class TopicTypeConverter {

    @TypeConverter
    fun toStatusString(status: Topic.Status): String {
        return status.toString()
    }

    @TypeConverter
    fun toStatusType(status: String): Topic.Status {
        return try {
            Topic.Status.valueOf(status)
        } catch (e: IllegalArgumentException) {
            Topic.Status.UNKNOWN
        }
    }

}
