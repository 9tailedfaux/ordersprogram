package models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.file.Files
import java.nio.file.Paths

class DataParserUtility {
    private val gson = Gson()

    fun matchItems(initialOrder: InitialOrder, menu: Menu): FinalOrder {
        val list = ArrayList<Item>()
        val meal = menu.meals[initialOrder.meal]!!
        for ((index, courseNumber) in initialOrder.courses.withIndex()) {
            val item = meal.courses[courseNumber]!!.items[index]!!
            list.add(item)
        }

        //add defaults if they arent already there
        for (courseNumber in initialOrder.courses.distinct()) {
            meal.courses[courseNumber]!!.items.forEach { itemEntry ->
                if (itemEntry.value.provided
                    && !list.any {
                        it.number == itemEntry.value.number
                    }
                ) {
                    list.add(itemEntry.value)
                }
            }
        }

        list.sortBy { it.course.number }
        return FinalOrder(initialOrder.meal, list)
    }

    fun checkOrderValidity(initialOrder: InitialOrder, menu: Menu, onError: (error: String) -> Unit): Boolean {
        if (!checkMealExists(initialOrder, menu)) {
            onError("${initialOrder.meal} is not on the menu")
            return false
        }
        if (!checkCoursesExist(initialOrder, menu, onError)) {
            return false
        }
        if (!checkItemsMatchCourses(initialOrder, menu, onError)) {
            return false
        }
        if (!checkDuplicateValidity(initialOrder, menu, onError)) {
            return false
        }
        return true
    }

    private fun checkCoursesExist(initialOrder: InitialOrder, menu: Menu, onError: (error: String) -> Unit): Boolean {
        for (course in initialOrder.courses) {
            if (!menu.meals[initialOrder.meal]!!.courses.containsKey(course)) {
                onError("Course $course is not on the menu")
                return false
            }
        }
        return true
    }

    private fun checkItemsMatchCourses(initialOrder: InitialOrder, menu: Menu, onError: (error: String) -> Unit): Boolean {
        if (initialOrder.items.size != initialOrder.courses.size) {
            onError("Every item must have a course and vice versa")
            return false
        }
        for ((index, item) in initialOrder.items.withIndex()) {
            if (!menu.meals[initialOrder.meal]!!.courses[index]!!.items.any { it.value.number == item }) {
                onError("Course ${initialOrder.courses[index]} does not contain item $item")
                return false
            }
        }
        return true
    }

    private fun checkMealExists(initialOrder: InitialOrder, menu: Menu): Boolean {
        if (!menu.meals.keys.contains(initialOrder.meal)) {
            return false
        }
        return true
    }

    private fun checkDuplicateValidity(initialOrder: InitialOrder, menu: Menu, onError: (error: String) -> Unit): Boolean {
        for ((index, itemNum) in initialOrder.items.withIndex()) {
            val item = menu.meals[initialOrder.meal]!!.courses[initialOrder.courses[index]]!!.items[itemNum]!!
            if (initialOrder.itemCounts[itemNum]!! > 1 && !item.multipleAllowed) {
                onError("You cannot have more than one ${item.name}")
                return false
            }
        }

        for (courseNum in initialOrder.courses) {
            val course = menu.meals[initialOrder.meal]!!.courses[courseNum]!!
            if (initialOrder.courseCounts[courseNum]!! > 1 && !course.multipleAllowed) {
                onError("You cannot have more than one ${course.name}")
                return false
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

    fun parseItemsCourses(items: String): IntArray = items.split(",").map { it.toInt() }.toIntArray()

    companion object {
        private var instance: DataParserUtility = DataParserUtility()

        fun getInstance(): DataParserUtility {
            return instance
        }
    }
}