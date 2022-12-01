import models.DataParserUtility
import kotlin.system.exitProcess

/*
hi!
the sample data is very limiting since each part (henceforth called a "course") has only one possible food item,
with the water being a weird exception.
thus the core data parsing is included in the abstractdataparserutility
this is inherited by dataparserutility
you should use this one.
there is an abstract function for parsing courses but i have not filled it in as they are indistinct
were you to extend this and fill in the logic for that
in this case, you would also have to redo the driver (this main function)
 */

fun main(args: Array<String>) {
    var items: IntArray
    var menu: String
    val parser = DataParserUtility.getInstance()

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
            items = parser.parseItems(args.last())
            menu = DEFAULT_MENU
        }
        else -> {
            try {
                args.last().toInt()
                items = parser.parseItems(args.copyOfRange(1, args.size))
                menu = DEFAULT_MENU
            } catch (e: NumberFormatException) {
                menu = args.last()
                items = parser.parseItems(args.copyOfRange(1, args.lastIndex))
            }
        }
    }

    //since items == courses in the given project, the same value is passed
    Application(meal = args.first(), items = items, menu = menu, courses = items).start()
}

const val DEFAULT_MENU = "menu.json"