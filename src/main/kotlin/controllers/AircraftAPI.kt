package controllers

import models.Aircraft
import utils.Utilities.isValidListIndex

class AircraftAPI {

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

}