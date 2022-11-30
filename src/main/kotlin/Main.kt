import kotlin.system.exitProcess

fun main(args: Array<String>) {
    var items: IntArray
    var menu: String

    when (args.size) {
        0 -> {
            println("Usage: ordersprogram [meal] [items] [alternative menu json (optional)]")
            exitProcess(0)
        }
        1 -> {
            println("Too few arguments. You must order something")
            exitProcess(1)
        }
        2 -> {
            items = parseItems(args.last())
            menu = DEFAULT_MENU
        }
        else -> {
            try {
                args.last().toInt()
                items = parseItems(args.copyOfRange(1, args.size))
                menu = DEFAULT_MENU
            } catch (e: NumberFormatException) {
                menu = args.last()
                items = parseItems(args.copyOfRange(1, args.lastIndex))
            }
        }
    }

    Application(meal = args.first(), items, menu)
}

const val DEFAULT_MENU = "menu.json"

fun parseItems(items: Array<String>): IntArray {
    var string = ""
    for (item in items) {
        string += item
    }
    return parseItems(string)
}

fun parseItems(items: String): IntArray = items.split(",").map { it.toInt() }.toIntArray().also { it.sort() }