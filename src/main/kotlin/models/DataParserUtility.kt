package models

import com.google.gson.JsonParser
import java.nio.file.Files
import java.nio.file.Paths

class DataParserUtility {

    fun processOrder(initialOrder: InitialOrder, menu: Menu): FinalOrder {
        val list = ArrayList<Item>()
        val meal = menu.meals[initialOrder.meal]!!
        for ((index, courseNumber) in initialOrder.courses.withIndex()) {
            val item = meal.courses[courseNumber]!!.items[initialOrder.items[index]]!!
            item.count = initialOrder.itemCounts[item.number]!!
            list.add(item)
        }

        //add defaults if they aren't already there
        meal.courses.values.forEach { course ->
            meal.courses[course.number]!!.items.values.forEach { itemEntry ->
                if (
                    (itemEntry.default
                            && !list.any {
                                it.number == itemEntry.number || it.course.number == itemEntry.course.number
                            })
                    || itemEntry.provided
                ) {
                    list.add(itemEntry)
                }
            }
        }

        list.sortBy { it.course.number }
        return FinalOrder(initialOrder, list)
    }

    fun checkOrderValidity(initialOrder: InitialOrder, menu: Menu, onError: (error: String) -> Unit): Boolean {
        if (!checkMealExists(initialOrder, menu)) {
            onError("${initialOrder.meal} is not on the menu")
            return false
        }
        if (!checkCoursesExist(initialOrder, menu, onError)) {
            return false
        }
        if (!checkRequiredCourses(initialOrder, menu, onError)) {
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

    private fun checkRequiredCourses(initialOrder: InitialOrder, menu: Menu, onError: (error: String) -> Unit): Boolean {
        menu.meals[initialOrder.meal]!!.courses.values.filter { it.required }
            .forEach {
                if (!initialOrder.courses.contains(it.number)) {
                    onError("${it.name} is required")
                    return false
                }
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
            try {
                if (!menu.meals[initialOrder.meal]!!.courses[initialOrder.courses[index]]!!.items.any { it.value.number == item }) {
                    onError("Course ${initialOrder.courses[index]} does not contain item $item")
                    return false
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
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

        for ((index, courseNum) in initialOrder.courses.withIndex()) {
            val course = menu.meals[initialOrder.meal]!!.courses[courseNum]!!
            if (initialOrder.courseCounts[courseNum]!! > 1 && !course.multipleAllowed) {
                val item = menu.meals[initialOrder.meal]!!.courses[courseNum]!!.items[initialOrder.items[index]]!!
                if (!item.multipleAllowed) {
                    onError("You cannot have more than one ${course.name}")
                    return false
                }
            }
        }

        return true
    }

    fun parseMenu(menu: String): HashMap<String, Meal> {
        val reader = Files.newBufferedReader(Paths.get(menu))
        val data = JsonParser.parseReader(reader).asJsonArray
        val map = HashMap<String, Meal>()
        for (iMeal in data) {
            //parse meals
            val meal = iMeal.asJsonObject
            val mealName = meal.get("name").asString
            val mealCourses = HashMap<Int, Course>()
            for (iCourse in meal.get("courses").asJsonArray) {
                //parse courses
                val course = iCourse.asJsonObject
                val courseName = course.get("name").asString
                val courseNumber = course.get("number").asInt
                val courseRequired = course.get("required").asBoolean
                val courseMultipleAllowed = course.get("multiple_allowed").asBoolean
                val courseItems = HashMap<Int, Item>()
                for (iItem in course.get("items").asJsonArray) {
                    //parse items
                    val item = iItem.asJsonObject
                    val itemName = item.get("name").asString
                    val itemNumber = item.get("number").asInt
                    val itemMultipleAllowed = item.get("multiple_allowed").asBoolean
                    val itemProvided = item.get("provided").asBoolean
                    val itemDefault = item.get("default").asBoolean
                    val itemUserSpecified = item.get("user_specified").asBoolean
                    courseItems[itemNumber] = Item(
                        itemName,
                        itemNumber,
                        itemMultipleAllowed,
                        itemProvided,
                        itemDefault,
                        itemUserSpecified
                    )
                }
                mealCourses[courseNumber] = Course(
                    courseItems,
                    courseName,
                    courseNumber,
                    courseRequired,
                    courseMultipleAllowed
                )
            }
            map[mealName] = Meal(
                mealCourses,
                mealName
            )
        }
        return map
    }

    fun parseItemsCourses(items: String): IntArray = items.split(",").map {
        it.toInt()
    }.toIntArray()

    companion object {
        private var instance: DataParserUtility = DataParserUtility()

        fun getInstance(): DataParserUtility {
            return instance
        }
    }
}