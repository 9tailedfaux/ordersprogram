package models

data class Course(
    val items: HashMap<Int, Item>,
    val name: String,
    val number: Int,
    val required: Boolean,
    val multipleAllowed: Boolean
) {
    init {
        for (item in items.values) {
            item.course = this
        }
    }
    override fun toString(): String {
        var string = "$name: ("
        for ((index, item) in items.values.withIndex()) {
            string += item.toString()
            if (index < items.values.size - 1) string += ", "
        }
        string += ")"
        return string
    }
}
