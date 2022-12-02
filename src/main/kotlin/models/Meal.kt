package models

data class Meal(
    val courses: HashMap<Int, Course>,
    val name: String
) {
    override fun toString(): String {
        var string = "$name: "
        for ((index, course) in courses.values.withIndex()) {
            string += course.toString()
            if (index < courses.values.size - 1) string += ", "
        }
        return string
    }
}
