package models

data class Item(
    val name: String,
    val type: String,
    val number: Int,
    val course: Course,
    val multipleAllowed: Boolean,
    val provided: Boolean
)