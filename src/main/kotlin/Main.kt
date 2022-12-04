import models.DataParserUtility
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val items: IntArray
    var courses: IntArray? = null
    val menu: String
    val parser = DataParserUtility.getInstance()

    when (args.size) {
        0 -> {
            println("Usage: ordersprogram [meal] [item numbers] [course numbers] [alternative menu json (optional)]" +
                    "\n\tItem numbers: comma separated, no spaces" +
                    "\n\tCourse numbers: optional. comma separated, no spaces. corresponds to items. if omitted, item numbers will be used" +
                    "\n\tAlternative menu: path to a .json file. optional. if omitted, default menu will be used" +
                    "\n\t\tdefault menu json can be found in menu.json in this project")
            exitProcess(0)
        }
        1 -> {
            println("Too few arguments. You must order something")
            exitProcess(0)
        }
        2 -> {
            items = parser.parseItemsCourses(args[1])
            menu = DEFAULT_MENU
        }
        in 3..4 -> {
            menu = if (args.last().last().isLetter()) {
                args.last()
            } else {
                DEFAULT_MENU
            }
            items = parser.parseItemsCourses(args[1])
            if (args.size == 4) {
                courses = parser.parseItemsCourses(args[2])
            }
        }
        else -> {
            println("Too many arguments. What is you doing?")
            exitProcess(0)
        }
    }

    //since items == courses in the given project, the same value is passed
    Application(meal = args.first(), items = items, menu = menu, courses = courses ?: items).start()
}

const val DEFAULT_MENU = "menu.json"