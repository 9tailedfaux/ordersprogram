import models.DataParserUtility

class Application(val meal: String, val items: IntArray, menu: String) {
    val parser = DataParserUtility()
    val parsedMenu = parser.parseMenu(menu)
}