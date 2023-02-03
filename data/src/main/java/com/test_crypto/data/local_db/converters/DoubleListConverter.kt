package com.test_crypto.data.local_db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class DoubleListConverter {
    @TypeConverter
    fun fromString(value: String?): List<List<Double>>? {
        println("DoubleListConverter fromString value " + value)
        if(value == null)
            return null

        return try {
            var checkedValue = value
            if (!value.startsWith("["))
                checkedValue = "[$checkedValue"
            if(!value.endsWith("]"))
                checkedValue = "$checkedValue]"
            val listType = object : TypeToken<List<List<Double>>>() {}.type
            Gson().fromJson(checkedValue, listType)
        } catch (e : Exception){
            mutableListOf()
        }
    }

    @TypeConverter
    fun fromArrayList(list: List<List<Double>>?): String? {
        return if(list != null){
            val gson = Gson()
            gson.toJson(list)
        } else {
            return null
        }

    }
}