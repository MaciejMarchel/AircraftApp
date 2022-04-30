package controllers

import models.Aircraft
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
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
    private var populatedAircraft: AircraftAPI? = AircraftAPI(XMLSerializer(File("aircraft.xml")))
    private var emptyAircraft: AircraftAPI? = AircraftAPI(XMLSerializer(File("aircraft.xml")))

    @BeforeEach
    fun setup() {
        Cessna = Aircraft("Cessna 340", "Turboprop Engine", 200000.00, "Textron Aviation", true)
        Boeing = Aircraft("BBJ 737", "Turbojet Engine", 2500000.00, "Boeing", false)
        testAircraft = Aircraft("test", "Turboprop Engine", 10000.00, "Test", false)
        Airbus = Aircraft("ACJ350", "Turbojet Engine", 2000000.00, "Airbus", true)
        Comac = Aircraft("Comac C919", "Turbojet Engine", 1800000.00, "Comac", true)

        //adding 5 aircraft to the aircrafts api
        populatedAircraft!!.add(Cessna!!)
        populatedAircraft!!.add(Boeing!!)
        populatedAircraft!!.add(testAircraft!!)
        populatedAircraft!!.add(Airbus!!)
        populatedAircraft!!.add(Comac!!)
    }

    @AfterEach
    fun tearDown() {
        Cessna = null
        Boeing = null
        testAircraft = null
        Airbus = null
        Comac = null
        populatedAircraft = null
        emptyAircraft = null
    }

    //Testing adding function
    @Test
    fun `adding an Aircraft to a populated list adds to ArrayList`() {
        val newAircraft = Aircraft("Cessna 172", "Turboprop Engine", 8000.00, "Textron Aviation", true)
        assertTrue(populatedAircraft!!.add(newAircraft))
    }

    @Test
    fun `adding an Aircraft to an empty list adds to ArrayList`() {
        val newAircraft = Aircraft("Cessna 172", "Turboprop Engine", 8000.00, "Textron Aviation", false)
        assertTrue(emptyAircraft!!.add(newAircraft))
    }

    //Testing List function
    @Nested
    inner class ListAircraft {
        //List all aircraft test
        @Test
        fun `listAllAircraft returns No Aircraft Stored message when ArrayList is empty`() {
            assertEquals(0, emptyAircraft!!.numberOfAircrafts())
            assertTrue(emptyAircraft!!.listAllAircraft().lowercase().contains("no aircraft"))
        }

        @Test
        fun `listAllAircraft returns Aircraft when ArrayList has aircraft stored`() {
            assertEquals(5, populatedAircraft!!.numberOfAircrafts())
            val aircraftString = populatedAircraft!!.listAllAircraft().lowercase()
            assertTrue(aircraftString.contains("cessna 340"))
            assertTrue(aircraftString.contains("bbj 737"))
            assertTrue(aircraftString.contains("test"))
            assertTrue(aircraftString.contains("acj350"))
            assertTrue(aircraftString.contains("comac c919"))
        }

        @Test
        fun `listAvailableAircraft returns no available aircraft stored when ArrayList is empty`() {
            assertEquals(0, emptyAircraft!!.numberOfAvailableAircraft())
            assertTrue(
                emptyAircraft!!.listAvailableAircraft().lowercase().contains("no available aircraft")
            )
        }

        //List all available aircraft test
        @Test
        fun `listAvailableAircraft returns available aircraft when ArrayList has available aircraft stored`() {
            assertEquals(3, populatedAircraft!!.numberOfAvailableAircraft())
            val activeaircraftString = populatedAircraft!!.listAvailableAircraft().lowercase()
            assertTrue(activeaircraftString.contains("cessna 340"))
            assertFalse(activeaircraftString.contains("bbj 737"))
            assertFalse(activeaircraftString.contains("test"))
            assertTrue(activeaircraftString.contains("acj350"))
            assertTrue(activeaircraftString.contains("comac c919"))
        }

        //List all unavailable aircraft test
        @Test
        fun `listUnavailableAircraft returns no archived aircraft when ArrayList is empty`() {
            assertEquals(0, emptyAircraft!!.numberOfUnavailableAircraft())
            assertTrue(
                emptyAircraft!!.listUnavailableAircraft().lowercase().contains("no Unavailable aircraft")
            )
        }

        @Test
        fun `listUnavailableAircraft returns Unavailable aircraft when ArrayList has Unavailable aircraft stored`() {
            assertEquals(2, populatedAircraft!!.numberOfUnavailableAircraft())
            val UnavailableAircraftString = populatedAircraft!!.listUnavailableAircraft().lowercase(Locale.getDefault())
            assertFalse(UnavailableAircraftString.contains("cessna 340"))
            assertTrue(UnavailableAircraftString.contains("bbj 737"))
            assertTrue(UnavailableAircraftString.contains("test"))
            assertFalse(UnavailableAircraftString.contains("acj350"))
            assertFalse(UnavailableAircraftString.contains("comac c919"))
        }
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
        fun `updating a aircraft that does not exist returns false`() {
            assertFalse(
                populatedAircraft!!.updateAircraft(
                    6,
                    Aircraft("Updating aircraft", "Turboprop Engine", 200000.00, "Textron Aviation", true)
                )
            )
            assertFalse(
                populatedAircraft!!.updateAircraft(
                    -1,
                    Aircraft("Updating aircraft", "Turboprop Engine", 15000.00, "Boeing", false)
                )
            )
            assertFalse(
                emptyAircraft!!.updateAircraft(
                    0,
                    Aircraft("Updating aircraft", "Turboprop Engine", 200000.00, "Textron Aviation", false)
                )
            )
        }

        @Test
        fun `updating a aircraft that exists returns true and updates`() {
            //check aircraft 5 exists and check the contents
            assertEquals(Comac, populatedAircraft!!.findAircraft(4))
            assertEquals("Comac C919", populatedAircraft!!.findAircraft(4)!!.airName)
            assertEquals("Comac", populatedAircraft!!.findAircraft(4)!!.airMake)
            assertEquals("Turbojet Engine", populatedAircraft!!.findAircraft(4)!!.airType)

            //update aircraft 5 with new information and ensure contents updated successfully
            assertTrue(
                populatedAircraft!!.updateAircraft(
                    4,
                    Aircraft("Updating aircraft", "Turboprop Engine", 20000.00, "Textron Aviation", true)
                )
            )
            assertEquals("Updating aircraft", populatedAircraft!!.findAircraft(4)!!.airName)
            assertEquals("Textron Aviation", populatedAircraft!!.findAircraft(4)!!.airMake)
            assertEquals("Turboprop Engine", populatedAircraft!!.findAircraft(4)!!.airType)
        }
    }

    //Persistence Testing
    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty aircrafts.XML file.
            val storingAircraft = AircraftAPI(XMLSerializer(File("aircrafts.xml")))
            storingAircraft.store()

            //Loading the empty aircrafts.xml file into a new object
            val loadedAircraft = AircraftAPI(XMLSerializer(File("aircrafts.xml")))
            loadedAircraft.load()

            //Comparing the source of the aircraft (storingAircrafts) with the XML loaded aircraft (loadedAircrafts)
            assertEquals(0, storingAircraft.numberOfAircrafts())
            assertEquals(0, loadedAircraft.numberOfAircrafts())
            assertEquals(storingAircraft.numberOfAircrafts(), loadedAircraft.numberOfAircrafts())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 aircraft to the aircrafts.XML file.
            val storingAircraft = AircraftAPI(XMLSerializer(File("aircrafts.xml")))
            storingAircraft.add(Airbus!!)
            storingAircraft.add(Comac!!)
            storingAircraft.add(Boeing!!)
            storingAircraft.store()

            //Loading aircrafts.xml into a different collection
            val loadedAircraft = AircraftAPI(XMLSerializer(File("aircrafts.xml")))
            loadedAircraft.load()

            //Comparing the source of the aircraft (storingAircrafts) with the XML loaded aircraft (loadedAircrafts)
            assertEquals(3, storingAircraft.numberOfAircrafts())
            assertEquals(3, loadedAircraft.numberOfAircrafts())
            assertEquals(storingAircraft.numberOfAircrafts(), loadedAircraft.numberOfAircrafts())
            assertEquals(storingAircraft.findAircraft(0), loadedAircraft.findAircraft(0))
            assertEquals(storingAircraft.findAircraft(1), loadedAircraft.findAircraft(1))
            assertEquals(storingAircraft.findAircraft(2), loadedAircraft.findAircraft(2))
        }
    }

    //JSON
    @Test
    fun `saving and loading an empty collection in JSON doesn't crash app`() {
        // Saving an empty aircrafts.json file.
        val storingAircraft = AircraftAPI(JSONSerializer(File("aircrafts.json")))
        storingAircraft.store()

        //Loading the empty aircrafts.json file into a new object
        val loadedAircraft = AircraftAPI(JSONSerializer(File("aircrafts.json")))
        loadedAircraft.load()

        //Comparing the source of the aircraft (storingAircrafts) with the json loaded aircraft (loadedAircrafts)
        assertEquals(0, storingAircraft.numberOfAircrafts())
        assertEquals(0, loadedAircraft.numberOfAircrafts())
        assertEquals(storingAircraft.numberOfAircrafts(), loadedAircraft.numberOfAircrafts())
    }

    @Test
    fun `saving and loading an loaded collection in JSON doesn't loose data`() {
        // Storing 3 aircraft to the aircrafts.json file.
        val storingAircraft = AircraftAPI(JSONSerializer(File("aircrafts.json")))
        storingAircraft.add(Airbus!!)
        storingAircraft.add(Comac!!)
        storingAircraft.add(Boeing!!)
        storingAircraft.store()

        //Loading aircrafts.json into a different collection
        val loadedAircraft = AircraftAPI(JSONSerializer(File("aircrafts.json")))
        loadedAircraft.load()

        //Comparing the source of the aircraft (storingAircrafts) with the json loaded aircraft (loadedAircrafts)
        assertEquals(3, storingAircraft.numberOfAircrafts())
        assertEquals(3, loadedAircraft.numberOfAircrafts())
        assertEquals(storingAircraft.numberOfAircrafts(), loadedAircraft.numberOfAircrafts())
        assertEquals(storingAircraft.findAircraft(0), loadedAircraft.findAircraft(0))
        assertEquals(storingAircraft.findAircraft(1), loadedAircraft.findAircraft(1))
        assertEquals(storingAircraft.findAircraft(2), loadedAircraft.findAircraft(2))
    }
    //Test Counting methods
    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfAircraftCalculatedCorrectly() {
            assertEquals(5, populatedAircraft!!.numberOfAircrafts())
            assertEquals(0, emptyAircraft!!.numberOfAircrafts())
        }

        @Test
        fun numberOfAvailableAircraftCalculatedCorrectly() {
            assertEquals(3, populatedAircraft!!.numberOfAvailableAircraft())
            assertEquals(0, emptyAircraft!!.numberOfAvailableAircraft())
        }

        @Test
        fun numberOfUnavailableAircraftCalculatedCorrectly() {
            assertEquals(2, populatedAircraft!!.numberOfUnavailableAircraft())
            assertEquals(0, emptyAircraft!!.numberOfUnavailableAircraft())
        }
    }

    //Testing Searches
    @Nested
    inner class SearchMethods {

        @Test
        fun `search aircraft's by name, returns no aircraft when no aircraft with that name exist`() {
            //Searching a populated collection for a name that doesn't exist.
            assertEquals(5, populatedAircraft!!.numberOfAircrafts())
            val searchResults = populatedAircraft!!.searchByName("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptyAircraft!!.numberOfAircrafts())
            assertTrue(emptyAircraft!!.searchByName("").isEmpty())
        }

        @Test
        fun `search aircraft by title returns aircraft when aircraft with that title exists`() {
            assertEquals(5, populatedAircraft!!.numberOfAircrafts())

            //Searching a populated collection for a full name that exists (case matches exactly)
            var searchResults = populatedAircraft!!.searchByName("BBJ 737")
            assertTrue(searchResults.contains("BBJ 737"))
            assertFalse(searchResults.contains("test"))

            //Searching a populated collection for a partial name that exists (case matches exactly)
            searchResults = populatedAircraft!!.searchByName("737")
            assertTrue(searchResults.contains("BBJ 737"))
            assertFalse(searchResults.contains("test"))

            //Searching a populated collection for a partial name that exists (case doesn't match)
            searchResults = populatedAircraft!!.searchByName("bBj")
            assertTrue(searchResults.contains("BBJ 737"))
            assertFalse(searchResults.contains("test"))
        }
    }
}