package models

class DataParserUtility: AbstractDataParserUtility() {


    companion object {
        private var instance: DataParserUtility = DataParserUtility()

        fun getInstance(): DataParserUtility {
            return instance
        }
    }

    //see comment in Main.kt for what the heck this thing is
    override fun parseCourses(courses: String): IntArray {
        return IntArray(0)
    }
}