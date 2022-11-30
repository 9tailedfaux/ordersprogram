package models

data class Order(
    val items: IntArray,
    val courses: IntArray,
    val meal: String
) {
    val itemCounts = HashMap<Int, Int>()
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