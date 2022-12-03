import models.DataParserUtility
import models.Menu
import models.InitialOrder

class Application(meal: String, items: IntArray, menu: String, courses: IntArray) {
    private val parser = DataParserUtility.getInstance()
    private val parsedMenu = Menu(parser.parseMenu(menu))
    private val initialOrder = InitialOrder(items, ArrayList(courses.toList()), meal)

    fun start() {
        if (!parser.checkOrderValidity(initialOrder, parsedMenu) {
            println(it)
        }) return

        val finalOrder = parser.processOrder(initialOrder, parsedMenu)
        print(finalOrder.toString())
    }
}