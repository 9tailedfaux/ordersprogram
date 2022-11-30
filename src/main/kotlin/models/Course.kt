package models

data class Course(
    val item: Array<Item>,
    val name: String,
    val default: Item,
    val duplicatesAllowed: Boolean
)
