import models.DataParserUtility
import models.Menu
import models.Order

class Application(meal: String, items: IntArray, menu: String, courses: IntArray) {
    fun start() {
        if (!parser.checkOrderValidity(order, parsedMenu) {
            println(it)
        }) return


    }

    val parser = DataParserUtility.getInstance()
    val parsedMenu = Menu(parser.parseMenu(menu))
    val order = Order(items, courses, meal)
}