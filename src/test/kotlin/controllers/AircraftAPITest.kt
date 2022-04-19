package controllers

import models.Aircraft
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

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

        //adding 5 aircraft to the aircrafts api
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

    //Testing delete function
    @Nested
    inner class deleteAircrafts {

        @Test
        fun `deleting a aircraft that does not exist, returns null`() {
            assertNull(emptyAircraft!!.deleteAircraft(0))
            assertNull(populatedAircraft!!.deleteAircraft(-1))
            assertNull(populatedAircraft!!.deleteAircraft(5))
        }

        @Test
        fun `deleting a aircraft that exists delete and returns deleted object`() {
            assertEquals(5, populatedAircraft!!.numberOfAircrafts())
            assertEquals(Comac, populatedAircraft!!.deleteAircraft(4))
            assertEquals(4, populatedAircraft!!.numberOfAircrafts())
            assertEquals(Cessna, populatedAircraft!!.deleteAircraft(0))
            assertEquals(3, populatedAircraft!!.numberOfAircrafts())
        }
    }

    //Testing update function
    @Nested
    inner class UpdateAircrafts {
        @Test
        fun `updating a aircraft that does not exist returns false`(){
            assertFalse(populatedAircraft!!.updateAircraft(6, Aircraft("Updating aircraft", "Turboprop Engine", 200000.00, "Textron Aviation", true)))
            assertFalse(populatedAircraft!!.updateAircraft(-1, Aircraft("Updating aircraft", "Turboprop Engine", 15000.00, "Boeing", false)))
            assertFalse(emptyAircraft!!.updateAircraft(0, Aircraft("Updating aircraft", "Turboprop Engine", 200000.00,"Textron Aviation", false)))
        }

        @Test
        fun `updating a aircraft that exists returns true and updates`() {
            //check aircraft 5 exists and check the contents
            assertEquals(Comac, populatedAircraft!!.findAircraft(4))
            assertEquals("Comac C919", populatedAircraft!!.findAircraft(4)!!.airName)
            assertEquals("Comac", populatedAircraft!!.findAircraft(4)!!.airMake)
            assertEquals("Turbojet Engine", populatedAircraft!!.findAircraft(4)!!.airType)

            //update aircraft 5 with new information and ensure contents updated successfully
            assertTrue(populatedAircraft!!.updateAircraft(4, Aircraft("Updating aircraft","Turboprop Engine", 20000.00,"Textron Aviation", true)))
            assertEquals("Updating aircraft", populatedAircraft!!.findAircraft(4)!!.airName)
            assertEquals("Textron Aviation", populatedAircraft!!.findAircraft(4)!!.airMake)
            assertEquals("Turboprop Engine", populatedAircraft!!.findAircraft(4)!!.airType)
        }
    }
}