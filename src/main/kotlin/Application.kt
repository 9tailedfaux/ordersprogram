import models.DataParserUtility
import models.Menu
import models.InitialOrder

class Application(meal: String, items: IntArray, menu: String, courses: IntArray) {
    fun start() {
        if (!parser.checkOrderValidity(initialOrder, parsedMenu) {
            println(it)
        }) return


    }

    val parser = DataParserUtility.getInstance()
    val parsedMenu = Menu(parser.parseMenu(menu))
    val initialOrder = InitialOrder(items, courses, meal)
}