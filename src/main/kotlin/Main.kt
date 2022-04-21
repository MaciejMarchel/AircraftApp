import controllers.AircraftAPI
import models.Aircraft
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import java.util.*

val scanner = Scanner(System.`in`)
private val logger = KotlinLogging.logger {}
private val aircraftAPI = AircraftAPI(XMLSerializer(File("aircraft.xml")))

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
         > |   5) Search Aircraft           |
         > |   6) Search by Type            |
         > |   7) Search by Make            |
         > ----------------------------------
         > |  20) Save                      |
         > |  21) Load                      |
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
            5 -> searchAircraft()
            20 -> save()
            21 -> load()
            0 -> exitApp()
            else -> System.out.println("Invalid option was entered: ${option}")
        }
    } while (true)
}

/*
FUNCTIONS FOR THE MENU TO WORK
 */

fun addAircraft(){
    //logger.info { "Add an aircraft" }
    val airName = readNextLine("Enter a name of the aircraft: ")
    val airType = readNextLine("Enter the type of the aircraft: ")
    val airCost = readNextDouble("Enter the cost of the aircraft: ")
    val airMake = readNextLine("Enter the make of the aircraft: ")
    val isAdded = aircraftAPI.add(Aircraft(airName, airType, airCost, airMake, false))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listAircraft(){
    //logger.info { "List all aircraft" }
    println(aircraftAPI.listAllAircraft())
}

fun updateAircraft(){
    //logger.info { "Update an existing aircraft" }
    listAircraft()
    if (aircraftAPI.numberOfAircrafts() > 0) {
        //only ask user to choose aircraft if it exists
        val indexToUpdate = readNextInt("Enter the index of the aircraft to update: ")
        if (aircraftAPI.isValidIndex(indexToUpdate)) {
            val airName = readNextLine("Enter a name for the aircraft: ")
            val airType = readNextLine("Enter the type for the aircraft: ")
            val airMake = readNextLine("Enter the make for the aircraft: ")
            val airCost = readNextDouble("Enter the cost of the aircraft: ")

            //pass the index of the aircraft and new aircraft details to AircraftAPI for updating & to check success
            if (aircraftAPI.updateAircraft(indexToUpdate, Aircraft(airName,airType,airCost,airMake, false))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no aircraft for this index number")
        }
    }
}

fun deleteAircraft(){
    //logger.info { "Delete an aircraft" }
    if (aircraftAPI.numberOfAircrafts() > 0) {
        //only ask the user to choose an aircraft to delete if it exists
        val indexToDelete = readNextInt("Enter the index of the aircraft you wish to delete: ")
        //pass the index of the aircraft to the AircraftAPI for deletion and check if successful
        val aircraftToDelete = aircraftAPI.deleteAircraft(indexToDelete)
        if (aircraftToDelete != null) {
            println("Delete Successful! Deleted Aircraft: ${aircraftToDelete.airName}")
        } else {
            println("Deletion Failed")
        }
    }
}

//Search functions
fun searchAircraft() {
    val searchName = readNextLine("Enter the description to search by: ")
    val searchResults = aircraftAPI.searchByName(searchName)
    if (searchResults.isEmpty()) {
        println("No aircraft found")
    } else {
        println("searchResults")
    }
}

fun searchType() {
    val searchType = readNextLine("Enter the description to search by: ")
    val searchResults = aircraftAPI.searchByType(searchType)
    if (searchResults.isEmpty()) {
        println("No aircraft found")
    } else {
        println("searchResults")
    }
}

fun searchMake() {
    val searchMake = readNextLine("Enter the description to search by: ")
    val searchResults = aircraftAPI.searchByMake(searchMake)
    if (searchResults.isEmpty()) {
        println("No aircraft found")
    } else {
        println("searchResults")
    }
}

//Persistence functions
fun save() {
    try {
        aircraftAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        aircraftAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp(){
    logger.info { "Exiting the app" }
    exit(0)
}
