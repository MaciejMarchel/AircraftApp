package controllers

import models.Aircraft
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
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

    //TODO PERSISTENCE JUNIT TESTING
    /*
    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }

    //JSON
            @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

     */
}