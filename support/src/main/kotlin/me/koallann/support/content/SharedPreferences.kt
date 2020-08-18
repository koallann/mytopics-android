package me.koallann.support.content

import android.content.Context
import android.content.SharedPreferences

class SharedPreferences(context: Context) {

    private val client: SharedPreferences = context.getSharedPreferences(
        context.packageName + "_preferences",
        Context.MODE_PRIVATE
    )

    fun contains(key: String) = client.contains(key)

    fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        client.getBoolean(key, defaultValue)

    fun getInt(key: String, defaultValue: Int): Int = client.getInt(key, defaultValue)

    fun getFloat(key: String, defaultValue: Float): Float = client.getFloat(key, defaultValue)

    fun getLong(key: String, defaultValue: Long): Long = client.getLong(key, defaultValue)

    fun getString(key: String, defaultValue: String): String =
        client.getString(key, defaultValue) ?: defaultValue

    fun put(key: String, value: Any?) {
        val editor = client.edit()
        when (value) {
            null -> editor.remove(key)
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is String -> editor.putString(key, value)
            else -> throw IllegalArgumentException("Unsupported value type")
        }
        editor.apply()
    }

    fun remove(key: String) = client.edit().remove(key).apply()

    fun clear() = client.edit().clear().apply()

}
