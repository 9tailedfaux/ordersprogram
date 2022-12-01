package models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.file.Files
import java.nio.file.Paths

abstract class AbstractDataParserUtility {
    private val gson = Gson()

    fun checkOrderValidity(order: Order, menu: Menu, onError: (error: String) -> Unit): Boolean {
        if (!checkMealValidity(order, menu)) {
            onError("Invalid meal")
            return false
        }
        if (!checkDuplicateValidity(order, menu, onError)) {
            return false
        }
        return true
    }

    private fun checkMealValidity(order: Order, menu: Menu): Boolean {
        if (!menu.meals.keys.contains(order.meal)) {
            return false
        }
        return true
    }

    private fun checkDuplicateValidity(order: Order, menu: Menu, onError: (error: String) -> Unit): Boolean {
        for (item in order.itemCounts.keys) {
            for (course in order.courseCounts.keys) {
                if (menu.meals[order.meal]!!.courses.contains(item)) {
                    if (order.itemCounts[item]!! > 1) {
                        if (!menu.meals[order.meal]!!.courses[course]!!.items[item].multipleAllowed) {
                            onError("You can only have one ${menu.meals[order.meal]!!.courses[course]!!.items[item].name}")
                            return false
                        }
                    }
                }
                if (order.courseCounts[course]!! > 1) {
                    if (!menu.meals[order.meal]!!.courses[course]!!.multipleAllowed) {
                        onError("You can only have one ${menu.meals[order.meal]!!.courses[course]!!.name}")
                        return false
                    }
                }
            }
        }
        return true
    }

    fun parseMenu(menu: String): HashMap<String, Meal> {
        val reader = Files.newBufferedReader(Paths.get(menu))
        val list = gson.fromJson(reader, object : TypeToken<List<Meal>>() {} )
        val map = HashMap<String, Meal>()
        for (meal in list) {
            map[meal.name] = meal
        }
        return map
    }

    fun parseItems(items: Array<String>): IntArray {
        var string = ""
        for (item in items) {
            string += item
        }
        return parseItems(string)
    }

    fun parseItems(items: String): IntArray = items.split(",").map { it.toInt() }.toIntArray().also { it.sort() }

    abstract fun parseCourses(courses: String): IntArray
}