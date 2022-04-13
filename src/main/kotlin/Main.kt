import java.util.*
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit

val scanner = Scanner(System.`in`)

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return readNextInt(
        """ 
         > ----------------------------------
         > |        AIRCRAFT APP            |
         > ----------------------------------
         > | NOTE MENU                      |
         > |   1) Add an aircraft           |
         > |   2) List all aircraft         |
         > |   3) Update an aircraft        |
         > |   4) Delete an aircraft        |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">")
    )
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addAircraft()
            2 -> listAircraft()
            3 -> updateAircraft()
            4 -> deleteAircraft()
            0 -> exitApp()
            else -> System.out.println("Invalid option was entered: ${option}")
        }
    } while (true)
}

/*
FUNCTIONS FOR THE MENU TO WORK
 */

fun addAircraft(){
    println("Add an aircraft")
}

fun listAircraft(){
    println("List all aircraft")
}

fun updateAircraft(){
    println("Update an existing aircraft")
}

fun deleteAircraft(){
    println("Delete an aircraft")
}

fun exitApp(){
    println("Exiting the app")
    exit(0)
}
