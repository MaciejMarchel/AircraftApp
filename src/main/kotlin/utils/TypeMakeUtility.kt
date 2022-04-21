package utils

object TypeMakeUtility {

    //NOTE: JvmStatic annotation means that the categories variable is static (i.e. we can reference it through the class
    //      name; we don't have to create an object of TypeMakeUtility to use it.
    @JvmStatic
    val Type = setOf ("TurboProp", "TurboJet")  //add more text in here.

    @JvmStatic
    val airMake = setOf ("Textron Aviation", "Boeing", "Airbus", "Comac")

    @JvmStatic
    fun isValidType(typeToCheck: String?): Boolean {
        for (type in Type) {
            if (type.equals(typeToCheck, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isValidMake(makeToCheck: String?): Boolean {
        for (make in airMake) {
            if (make.equals(makeToCheck, ignoreCase = true)) {
                return true
            }

        }
        return false
    }
}