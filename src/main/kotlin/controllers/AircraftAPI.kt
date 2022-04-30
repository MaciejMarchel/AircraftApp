package controllers

import models.Aircraft
import persistence.Serializer
import utils.Utilities.isValidListIndex

/**
 * This class provides methods for the ArrayList that Main.kt can use to run the app
 * Includes a Serializer for persistence.
 *
 * @author Maciej Marchel
 * @version v0.2.0
 */

class AircraftAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var aircrafts = ArrayList<Aircraft>()

    /* Functions */
    /**
     * Adds an item to the ArrayList
     * Will add an item inputted by the user to the Arraylist
     *
     * @param prompt  Adds the Item to the ArrayList
     * @return The Item is added
     */
    fun add(aircraft: Aircraft): Boolean {
        return aircrafts.add(aircraft)
    }

    /**
     * Lists all Items that are in the ArrayList
     *
     * @param prompt Lists all Items for the user to read
     * @return The Listed Items into the console
     */
    fun listAllAircraft(): String =
        if (aircrafts.isEmpty()) "No aircraft stored"
        else formatListString(aircrafts)

    /**
     * Lists all Unavailable Items that are in the ArrayList
     *
     * @param prompt Lists all Unavailable Items for the user to read
     * @return The Listed Unavailable Items into the console
     */
    fun listUnavailableAircraft(): String =
        if (numberOfUnavailableAircraft() == 0) "No unavailable aircraft stored"
        else formatListString(aircrafts.filter { aircraft -> !aircraft.airAvailable })

    /**
     * Lists all Items that are Available in the ArrayList
     *
     * @param prompt Lists all Available Items  for the user to read
     * @return The Listed Available Items into the console
     */
    fun listAvailableAircraft(): String =
        if (numberOfAvailableAircraft() == 0) "No available aircraft stored"
        else formatListString(aircrafts.filter { aircraft -> aircraft.airAvailable })

    /**
     * Lists all Items sorted by descending (by cost)
     *
     * @param prompt Lists all Items descending (by cost) for the user to read
     * @return The descending (by cost) Listed Items into the console
     */
    // Sort by high to low cost
    fun listHighToLow(): String =
        formatListString(aircrafts.sortedByDescending { aircraft -> aircraft.airCost })

    /**
     * Lists all Items sorted by ascending (by cost)
     *
     * @param prompt Lists all Items ascending (by cost) for the user to read
     * @return The ascending (by cost) Listed Items into the console
     */
    // Sort by low to high cost
    fun listLowToHigh(): String =
        formatListString(aircrafts.sortedBy { aircraft -> aircraft.airCost })

    /**
     * Lists all Items sorted by alphabetical order (by name)
     *
     * @param prompt Lists all Items alphabetical order (by name) for the user to read
     * @return The Listed Items by alphabetical order (by name) into the console
     */
    // Sort by alphabetical order
    fun listByAlpha(): String =
        formatListString(aircrafts.sortedBy { aircraft -> aircraft.airName })

    /**
     * Lists the total amount of the Items
     *
     * @param prompt Lists all Items alphabetical order (by name) for the user to read
     * @return The total amount of items in the ArrayList is listed as an Int
     */
    fun numberOfAircrafts(): Int {
        return aircrafts.size
    }

    /**
     * Deletes the item in the ArrayList by index (Int)
     *
     * @param prompt Allows the user to delete an item by inputting the index Int
     * @return Deletes an item by the chosen index with an Int
     */
    fun deleteAircraft(indexToDelete: Int): Aircraft? {
        return if (isValidListIndex(indexToDelete, aircrafts)) {
            aircrafts.removeAt(indexToDelete)
        } else null
    }

    /**
     * Finds the item with an Index
     *
     * @param prompt Allows the user to use and Index to find an Item
     * @return The item is found by inputting an index
     */
    // find aircraft
    fun findAircraft(index: Int): Aircraft? {
        return if (isValidListIndex(index, aircrafts)) {
            aircrafts[index]
        } else null
    }

    /**
     * Allows the user to update the fields of the item without having to create a new item.
     * The user inputs an Int to update the Item by Index
     *
     * @param prompt Allows the user to update an item using an index
     * @return The item is to be updated is chosen by inputting an index and allows for updating the fields
     */
    fun updateAircraft(indexToUpdate: Int, aircraft: Aircraft?): Boolean {
        // find the aircraft object by index number
        val foundAircraft = findAircraft(indexToUpdate)

        // if the aircraft exists use the details passed as parameters to update the found aircraft in the ArrayList.
        if ((foundAircraft != null) && (aircraft != null)) {
            foundAircraft.airName = aircraft.airName
            foundAircraft.airType = aircraft.airType
            foundAircraft.airMake = aircraft.airMake
            foundAircraft.airCost = aircraft.airCost
            return true
        }

        // if the aircraft was not found return false to indicate the update didnt happen
        return false
    }

    /**
     * Allows the user to search for a specific item by name.
     *
     * @param prompt Allows the user to search for an item by name and is printed to the console for the user to read
     * @return The search looks for the item by airName variable and prints it out to the console.
     */
    // Searching functions
    fun searchByName(searchString: String) =
        formatListString(
            aircrafts.filter { aircraft -> aircraft.airName.contains(searchString, ignoreCase = true) }
        )

    /**
     * Validation for the index using a utility class.
     *
     * @param prompt Allows for the validation of index
     * @return The index is validated as an Int.
     */
    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, aircrafts)
    }

    /**
     * Allows the user to search for a specific item by type.
     *
     * @param prompt Allows the user to search for an item by type and is printed to the console for the user to read
     * @return The search looks for the item by airType variable and prints it out to the console.
     */
    fun searchByType(searchString: String) =
        formatListString(
            aircrafts.filter { aircraft -> aircraft.airType.contains(searchString, ignoreCase = true) }
        )

    /**
     * Allows the user to search for a specific item by make.
     *
     * @param prompt Allows the user to search for an item by make and is printed to the console for the user to read
     * @return The search looks for the item by airmake variable and prints it out to the console.
     */
    fun searchByMake(searchString: String) =
        formatListString(
            aircrafts.filter { aircraft -> aircraft.airMake.contains(searchString, ignoreCase = true) }
        )

    // Helper Method
    /**
     * Helper method that formats the string for the items in the ArrayList
     *
     */
    private fun formatListString(aircraftToFormat: List<Aircraft>): String =
        aircraftToFormat
            .joinToString(separator = "\n") { aircraft ->
                aircrafts.indexOf(aircraft).toString() + ": " + aircraft.toString()
            }

    // Archive Listing Function + number of archived aircraft

    /**
     * Checks for the total count of Available Aircraft
     * It is a counting method and also used by listAllAvailableAircraft
     *
     * @param prompt used to check for the total of Available Aircraft
     * @return Returns the count of the Available Aircraft
     */
    fun numberOfAvailableAircraft(): Int = aircrafts.count { aircraft: Aircraft -> aircraft.airAvailable }

    /**
     * Checks for the total count of Unavailable Aircraft
     * It is a counting method and also used by listAllUnavailableAircraft
     *
     * @param prompt used to check for the total of Unavailable Aircraft
     * @return Returns the count of the Unavailable Aircraft
     */
    fun numberOfUnavailableAircraft(): Int = aircrafts.count { aircraft: Aircraft -> !aircraft.airAvailable }
    // fun numberOfCostAircraft(): Int = aircrafts. { aircraft: Aircraft -> aircraft.airCost }

    /**
     * Lists the aircraft to check if its available using the variable of airAvailable
     * Allows for the listing methods to work.
     */
    fun airListing(indexToAvailable: Int): Boolean {
        if (isValidIndex(indexToAvailable)) {
            val airToAvailable = aircrafts[indexToAvailable]
            if (!airToAvailable.airAvailable) {
                airToAvailable.airAvailable = true
                return true
            }
        }
        return false
    }

    // persistence
    /**
     * Loads the persistence file using a serializer
     */
    @Throws(Exception::class)
    fun load() {
        aircrafts = serializer.read() as ArrayList<Aircraft>
    }

    /**
     * Writes the persistence file using a serializer
     */
    @Throws(Exception::class)
    fun store() {
        serializer.write(aircrafts)
    }
}
