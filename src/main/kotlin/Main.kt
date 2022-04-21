import com.sun.jdi.event.ExceptionEvent
import controllers.AircraftAPI
import models.Aircraft
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.TypeMakeUtility.isValidMake
import java.io.File
import java.lang.System.exit
import java.util.*
import utils.TypeMakeUtility.isValidType
import java.io.IOException

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
         > |   2) List aircraft             |
         > |   3) Update an aircraft        |
         > |   4) Delete an aircraft        |
         > |   5) Make aircraft available   |
         > ----------------------------------
         > |   6) Search Aircraft           |
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
            2 -> listSubMenu()
            3 -> updateAircraft()
            4 -> deleteAircraft()
            5 -> availableAircraft()
            6 -> searchSubMenu()
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
        check(isValidType(typeToCheck = airType)) { "Please input the correct data for the field of Type" } //checks for the correct type inputted
        check(isValidMake(makeToCheck = airMake)) { "Please input the correct data for the field of Make" } //checks for the correct make inputted
    } else {
        println("Added Successfully")
    }
}

fun listSubMenu() {
    if (aircraftAPI.numberOfAircrafts() > 0) {
        val option = readNextInt(
            """
                  > ---------------------------------
                  > |   1) View ALL aircraft        |
                  > |   2) View UNAVAILABLE aircraft|
                  > |   3) View AVAILABLE aircraft  |
                  > |   4) View Cost by High to Low |
                  > |   5) View Cost by Low to High |
                  > ---------------------------------
         > ==>> """.trimMargin(">"))
        when (option) {
            1 -> listAircraft()
            2 -> listUnavailableAircraft()
            3 -> listAvailableAircraft()
            4 -> listByHighToLow()
            5 -> listByLowToHigh()
            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option invalid - No aircraft stored")
    }
}

fun searchSubMenu() {
    if (aircraftAPI.numberOfAircrafts() > 0) {
        val option = readNextInt(
            """
                  > ---------------------------------
                  > |   1) Search by name           |
                  > |   2) Search by type           |
                  > |   3) Search by make           |
                  > ---------------------------------
         > ==>> """.trimMargin(">"))
        when (option) {
            1 -> searchAircraft()
            2 -> searchType()
            3 -> searchMake()
            else -> println("Invalid option entered: " + option)
        }
    } else {
        println("Option invalid - No aircraft stored")
    }
}

fun listAircraft(){
    //logger.info { "List all aircraft" }
    println(aircraftAPI.listAllAircraft())
}

fun listUnavailableAircraft(){
    println(aircraftAPI.listUnavailableAircraft())
}

fun listAvailableAircraft(){
    println(aircraftAPI.listAvailableAircraft())
}

fun listByHighToLow(){
    println(aircraftAPI.listHighToLow())
}

fun listByLowToHigh(){
    println(aircraftAPI.listLowToHigh())
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
                check(isValidType(typeToCheck = airType)) { "Please input the correct data for the field of Type" } //checks for the correct type inputted
                check(isValidMake(makeToCheck = airMake)) { "Please input the correct data for the field of Make" } //checks for the correct make inputted
            } else {
                println("Update Successful")
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
    val searchName = readNextLine("Enter the NAME to search by: ")
    val searchResults = aircraftAPI.searchByName(searchName)
    if (searchResults.isEmpty()) {
        println("No aircraft found")
    } else {
        println(searchResults)
    }
}

fun searchType() {
    val searchType = readNextLine("Enter the TYPE to search by: ")
    val searchResults = aircraftAPI.searchByType(searchType)
    if (searchResults.isEmpty()) {
        println("No aircraft found")
    } else {
        println(searchResults)
    }
}

fun searchMake() {
    val searchMake = readNextLine("Enter the MAKE to search by: ")
    val searchResults = aircraftAPI.searchByMake(searchMake)
    if (searchResults.isEmpty()) {
        println("No aircraft found")
    } else {
        println(searchResults)
    }
}

//make available
fun availableAircraft() {
    listUnavailableAircraft()
    if (aircraftAPI.numberOfUnavailableAircraft() > 0) {
        //only ask the user to choose the aircraft to make available if unavailable aircraft exists
        val indexToAvailable = readNextInt("Enter the index of the aircraft to make available: ")
        //pass the index of the aircraft to AircraftAPI to make available and check for success
        if (aircraftAPI.airListing(indexToAvailable)) {
            println("Made Available")
        } else {
            println("Not successful please try again!")
        }
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
