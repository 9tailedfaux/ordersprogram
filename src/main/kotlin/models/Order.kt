package models

class InitialOrder(
    val items: IntArray,
    val courses: IntArray,
    meal: String
): BaseOrder(meal) {
    val courseCounts = HashMap<Int, Int>()

    init {
        for (item in items) {
            itemCounts[item] = itemCounts[item]?.plus(1) ?: 1
        }
        for (course in courses) {
            courseCounts[course] = courseCounts[course]?.plus(1) ?: 1
        }
    }
}

class FinalOrder(
    meal: String,
    val items: ArrayList<Item>
): BaseOrder(meal) {

    override fun toString(): String {
        var string = meal
        if (items.isEmpty()) return string + "contains no items"
        string += ": "
        var oldCourse = items[0].course.number
        for ((index, item) in items.withIndex()) {
            if (item.course.number != oldCourse) string += "${item.course.name}: ("
            string += item.name
            oldCourse = item.course.number
            //if this is the last item or the next item has a different course
            string += if (index == items.lastIndex || items[index + 1].course.number != oldCourse) {
                "), "
            } else {
                ", "
            }
        }
        return string
    }
    init {
        for (item in items) {
            itemCounts[item.number] = itemCounts[item.number]?.plus(1) ?: 1
        }
    }
}

abstract class BaseOrder (
    val meal: String
) {
    val itemCounts = HashMap<Int, Int>()
}