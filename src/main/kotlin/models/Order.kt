package models

class InitialOrder(
    val items: IntArray,
    val courses: ArrayList<Int>,
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
    initialOrder: InitialOrder,
    private val items: ArrayList<Item>
): BaseOrder(initialOrder.meal) {

    override fun toString(): String {
        var string = meal
        if (items.isEmpty()) return string + "contains no items"
        string += ": "
        val distinctItems = items.distinctBy { it.number }
        for ((index, item) in distinctItems.withIndex()) {
            string += item.toString()
            if (index < distinctItems.lastIndex) string += ", "
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