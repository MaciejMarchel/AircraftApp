package controllers

import models.Aircraft

class AircraftAPI {

    private var aircrafts = ArrayList<Aircraft>()

    /* Functions */
    fun add(aircraft: Aircraft): Boolean {
        return aircrafts.add(aircraft)
    }

    fun listAllAircraft(): String {
        return if (aircrafts.isEmpty()) {
            "No aircrafts stored"
        } else {
            var listOfAircraft = ""
            for (i in aircrafts.indices) {
                listOfAircraft += "${i}: ${aircrafts[i]} \n"
            }
            listOfAircraft
        }
    }
}