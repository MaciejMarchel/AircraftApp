
import controllers.AircraftAPI
import models.Aircraft
import mu.KotlinLogging
import persistence.XMLSerializer
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit
import java.util.Scanner

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
            else -> System.out.println("Invalid option was entered: $option")
        }
    } while (true)
}

/*
FUNCTIONS FOR THE MENU TO WORK
 */

fun addAircraft() {
    // logger.info { "Add an aircraft" }

    val airName = readNextLine("Enter a name of the aircraft: ")

    /*
    Had to use while loop to validate airType and airMake for adding and updating further down
    Old validation is commented out in the function(s)
     */
    var airType = ""

    var type2 = "TurboJet"
    var type1 = "TurboProp"

    while (airType.lowercase() != type1.lowercase() || airType.lowercase() != type2.lowercase()) {

        airType = readNextLine("Enter the type (TurboJet or TurboProp) of the aircraft: ")

        if (airType.lowercase() == type1.lowercase() || airType.lowercase() == type2.lowercase()) {
            break
        } else {
            println("Please enter either TurboJet or TurboProp")
        }
    }

    val airCost = readNextDouble("Enter the cost of the aircraft: ")

    var airMake = ""

    var make1 = "Textron Aviation"
    var make2 = "Boeing"
    var make3 = "Airbus"
    var make4 = "Comac"

    while (airMake.lowercase() != make1.lowercase() || airMake.lowercase() != make2.lowercase() || airMake.lowercase() != make3.lowercase() || airMake.lowercase() != make4.lowercase()) {

        airMake = readNextLine("Enter the make (Textron Aviation, Boeing, Airbus, Comac) of the aircraft: ")

        if (airMake.lowercase() == make1.lowercase() || airMake.lowercase() == make2.lowercase() || airMake.lowercase() == make3.lowercase() || airMake.lowercase() == make4.lowercase()) {
            break
        } else {
            println("Please enter either Textron Aviation, Boeing, Airbus or Comac")
        }
    }

    val isAdded = aircraftAPI.add(Aircraft(airName, airType, airCost, airMake, false))

    if (isAdded) {
        println("Added Successfully")
        // check(isValidType(typeToCheck = airType)) { "Please input the correct data for the field of Type (TurboProp, TurboJet)" } //checks for the correct type inputted
        // check(isValidMake(makeToCheck = airMake)) { "Please input the correct data for the field of Make (Textron Aviation, Boeing, Airbus, Comac)" } //checks for the correct make inputted
    } else {
        println("Failure to add aircraft")
    }
}

fun listSubMenu() {
    if (aircraftAPI.numberOfAircrafts() > 0) {
        val option = readNextInt(
            """
                  > ----------------------------------
                  > |   1) View ALL aircraft         |
                  > |   2) View UNAVAILABLE aircraft |
                  > |   3) View AVAILABLE aircraft   |
                  > |   4) View Cost by High to Low  |
                  > |   5) View Cost by Low to High  |
                  > |   6) Sort by alphabetical order|
                  > ----------------------------------
         > ==>> """.trimMargin(">")
        )
        when (option) {
            1 -> listAircraft()
            2 -> listUnavailableAircraft()
            3 -> listAvailableAircraft()
            4 -> listByHighToLow()
            5 -> listByLowToHigh()
            6 -> listByAlpha()
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
         > ==>> """.trimMargin(">")
        )
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

fun listAircraft() {
    // logger.info { "List all aircraft" }
    println(aircraftAPI.listAllAircraft())
}

fun listUnavailableAircraft() {
    println(aircraftAPI.listUnavailableAircraft())
}

fun listAvailableAircraft() {
    println(aircraftAPI.listAvailableAircraft())
}

fun listByHighToLow() {
    println(aircraftAPI.listHighToLow())
}

fun listByLowToHigh() {
    println(aircraftAPI.listLowToHigh())
}

fun listByAlpha() {
    println(aircraftAPI.listByAlpha())
}

fun updateAircraft() {
    // logger.info { "Update an existing aircraft" }
    listAircraft()
    if (aircraftAPI.numberOfAircrafts() > 0) {
        // only ask user to choose aircraft if it exists
        val indexToUpdate = readNextInt("Enter the index of the aircraft to update: ")
        if (aircraftAPI.isValidIndex(indexToUpdate)) {
            val airName = readNextLine("Enter a name for the aircraft: ")

            // Validation for airType
            var airType = ""

            var type2 = "TurboJet"
            var type1 = "TurboProp"

            while (airType.lowercase() != type1.lowercase() || airType.lowercase() != type2.lowercase()) {

                airType = readNextLine("Enter the type (TurboJet or TurboProp) of the aircraft: ")

                if (airType.lowercase() == type1.lowercase() || airType.lowercase() == type2.lowercase()) {
                    break
                } else {
                    println("Please enter either TurboJet or TurboProp")
                }
            }

            // Validation for airMake
            var airMake = ""

            var make1 = "Textron Aviation"
            var make2 = "Boeing"
            var make3 = "Airbus"
            var make4 = "Comac"

            while (airMake.lowercase() != make1.lowercase() || airMake.lowercase() != make2.lowercase() || airMake.lowercase() != make3.lowercase() || airMake.lowercase() != make4.lowercase()) {

                airMake = readNextLine("Enter the make (Textron Aviation, Boeing, Airbus, Comac) of the aircraft: ")

                if (airMake.lowercase() == make1.lowercase() || airMake.lowercase() == make2.lowercase() || airMake.lowercase() == make3.lowercase() || airMake.lowercase() == make4.lowercase()) {
                    break
                } else {
                    println("Please enter either Textron Aviation, Boeing, Airbus or Comac")
                }
            }

            val airCost = readNextDouble("Enter the cost of the aircraft: ")

            // pass the index of the aircraft and new aircraft details to AircraftAPI for updating & to check success
            if (aircraftAPI.updateAircraft(indexToUpdate, Aircraft(airName, airType, airCost, airMake, false))) {
                println("Update Successful")
                // check(isValidType(typeToCheck = airType)) { "Please input the correct data for the field of Type (TurboProp, TurboJet)" } //checks for the correct type inputted
                // check(isValidMake(makeToCheck = airMake)) { "Please input the correct data for the field of Make (Textron Aviation, Boeing, Airbus, Comac)" } //checks for the correct make inputted
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no aircraft for this index number")
        }
    }
}

fun deleteAircraft() {
    // logger.info { "Delete an aircraft" }
    if (aircraftAPI.numberOfAircrafts() > 0) {
        // only ask the user to choose an aircraft to delete if it exists
        val indexToDelete = readNextInt("Enter the index of the aircraft you wish to delete: ")
        // pass the index of the aircraft to the AircraftAPI for deletion and check if successful
        val aircraftToDelete = aircraftAPI.deleteAircraft(indexToDelete)
        if (aircraftToDelete != null) {
            println("Delete Successful! Deleted Aircraft: ${aircraftToDelete.airName}")
        } else {
            println("Deletion Failed")
        }
    }
}

// Search functions
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

// make available
fun availableAircraft() {
    listUnavailableAircraft()
    if (aircraftAPI.numberOfUnavailableAircraft() > 0) {
        // only ask the user to choose the aircraft to make available if unavailable aircraft exists
        val indexToAvailable = readNextInt("Enter the index of the aircraft to make available: ")
        // pass the index of the aircraft to AircraftAPI to make available and check for success
        if (aircraftAPI.airListing(indexToAvailable)) {
            println("Made Available")
        } else {
            println("Not successful please try again!")
        }
    }
}

// Persistence functions
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

fun exitApp() {
    logger.info { "Exiting the app" }
    exit(0)
}
