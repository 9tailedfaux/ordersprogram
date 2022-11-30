package models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.file.Files
import java.nio.file.Paths

class DataParserUtility {
    private val gson = Gson()

    fun parseMenu(menu: String): Array<Meal> {
        val reader = Files.newBufferedReader(Paths.get(menu))
        return gson.fromJson(reader, object : TypeToken<List<Meal>>() {} ).toTypedArray()
    }
}