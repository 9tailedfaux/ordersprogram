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

    companion object {
        fun checkOrderValidity(order: Order, menu: Menu): Boolean {
            if (!menu.meals.keys.contains(order.meal)) {
                return false
            }


            for (item in order.itemCounts.keys) {
                for (course in order.courseCounts.keys) {
                    if (menu.meals[order.meal]!!.courses.contains(item)) {
                        if (order.itemCounts[item]!! > 1) {
                            if (menu.meals[order.meal]!!.courses[item]!!.item[item].multipleAllowed) {
                                return false
                            }
                        }
                    }
                }
            }
        }
    }
}