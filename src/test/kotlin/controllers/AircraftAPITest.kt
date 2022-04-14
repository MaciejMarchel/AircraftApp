package controllers

import models.Aircraft
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AircraftAPITest {
    private var Cessna: Aircraft? = null
    private var Boeing: Aircraft? = null
    private var testAircraft: Aircraft? = null
    private var Airbus: Aircraft? = null
    private var Comac: Aircraft? = null
    private var populatedAircraft: AircraftAPI? = AircraftAPI()
    private var emptyAircraft: AircraftAPI? = AircraftAPI()

    @BeforeEach
    fun setup(){
        Cessna = Aircraft("Cessna 340", "Turboprop Engine",200000.00,"Textron Aviation", true)
        Boeing = Aircraft("BBJ 737", "Turbojet Engine", 2500000.00, "Boeing", false)
        testAircraft = Aircraft("test", "Turboprop Engine", 10000.00,"Test", false)
        Airbus = Aircraft("ACJ350","Turbojet Engine", 2000000.00, "Airbus", true)
        Comac = Aircraft("Comac C919", "Turbojet Engine", 1800000.00, "Comac", true)

        //adding 5 Note to the notes api
        populatedAircraft!!.add(Cessna!!)
        populatedAircraft!!.add(Boeing!!)
        populatedAircraft!!.add(testAircraft!!)
        populatedAircraft!!.add(Airbus!!)
        populatedAircraft!!.add(Comac!!)
    }

    @AfterEach
    fun tearDown(){
        Cessna = null
        Boeing = null
        testAircraft = null
        Airbus = null
        Comac = null
        populatedAircraft = null
        emptyAircraft = null
    }

    @Test
    fun `adding an Aircraft to a populated list adds to ArrayList`(){
        val newAircraft = Aircraft("Cessna 172", "Turboprop Engine", 8000.00, "Textron Aviation", true)
        assertTrue(populatedAircraft!!.add(newAircraft))
    }

    @Test
    fun `adding an Aircraft to an empty list adds to ArrayList`(){
        val newAircraft = Aircraft("Cessna 172", "Turboprop Engine", 8000.00, "Textron Aviation", false)
        assertTrue(emptyAircraft!!.add(newAircraft))
    }
}