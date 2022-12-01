package models

data class Course(
    val items: Array<Item>,
    val name: String,
    val default: Item,
    val multipleAllowed: Boolean
)
