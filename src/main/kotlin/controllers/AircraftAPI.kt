package controllers

import models.Aircraft
import persistence.Serializer
import utils.Utilities.isValidListIndex

class AircraftAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var aircrafts = ArrayList<Aircraft>()

    /* Functions */
    fun add(aircraft: Aircraft): Boolean {
        return aircrafts.add(aircraft)
    }

    fun listAllAircraft(): String {
        return if (aircrafts.isEmpty()) {
            "No aircraft's stored"
        } else {
            var listOfAircraft = ""
            for (i in aircrafts.indices) {
                listOfAircraft += "${i}: ${aircrafts[i]} \n"
            }
            listOfAircraft
        }
    }

    fun numberOfAircrafts(): Int {
        return aircrafts.size
    }

    fun deleteAircraft(indexToDelete: Int): Aircraft? {
        return if (isValidListIndex(indexToDelete, aircrafts)) {
            aircrafts.removeAt(indexToDelete)
        } else null
    }

    //find aircraft
    fun findAircraft(index: Int): Aircraft? {
        return if (isValidListIndex(index, aircrafts)) {
            aircrafts[index]
        } else null
    }

    fun updateAircraft(indexToUpdate: Int, aircraft: Aircraft?): Boolean {
        //find the aircraft object by index number
        val foundAircraft = findAircraft(indexToUpdate)

        //if the aircraft exists use the details passed as parameters to update the found aircraft in the ArrayList.
        if ((foundAircraft != null) && (aircraft != null)) {
            foundAircraft.airName = aircraft.airName
            foundAircraft.airType = aircraft.airType
            foundAircraft.airMake = aircraft.airMake
            foundAircraft.airCost = aircraft.airCost
            return true
        }

        //if the aircraft was not found return false to indicate the update didnt happen
        return false
    }

    //Searching functions
    fun searchByName (searchString : String) =
        formatListString(
            aircrafts.filter { aircraft -> aircraft.airName.contains(searchString, ignoreCase = true)  })

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, aircrafts);
    }

    fun searchByType (searchString: String) =
        formatListString(
            aircrafts.filter { aircraft -> aircraft.airType.contains(searchString, ignoreCase = true)  })

    fun searchByMake (searchString: String) =
        formatListString(
            aircrafts.filter { aircraft -> aircraft.airMake.contains(searchString, ignoreCase = true) })

    //Helper Method
    private fun formatListString(aircraftToFormat : List<Aircraft>) :String =
        aircraftToFormat
            .joinToString (separator = "\n") { aircraft ->
              aircrafts.indexOf(aircraft).toString() + ": " + aircraft.toString() }

    //persistence
    @Throws(Exception::class)
    fun load() {
        aircrafts = serializer.read() as ArrayList<Aircraft>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(aircrafts)
    }
}