package models

data class Item(
    val name: String,
    val number: Int,
    val multipleAllowed: Boolean,
    val provided: Boolean,
    val default: Boolean,
    val userSpecified: Boolean //todo: validate this
) {
    var count = 0
    lateinit var course: Course
    override fun toString(): String {
        return name
    }
}