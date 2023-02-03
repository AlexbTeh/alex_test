package com.test_crypto.data.local_db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class IntListConverter {
    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        if(value == null)
            return null

        return try {
            var checkedValue = value
            if (!value.startsWith("["))
                checkedValue = "[$checkedValue"
            if(!value.endsWith("]"))
                checkedValue = "$checkedValue]"
            val listType = object : TypeToken<List<Int>>() {}.type
            Gson().fromJson(checkedValue, listType)
        } catch (e : Exception){
            mutableListOf()
        }
    }

    @TypeConverter
    fun fromArrayList(list: List<Int>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}