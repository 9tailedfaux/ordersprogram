package models

data class Meal(
    val courses: HashMap<Int, Course>,
    val name: String
)
