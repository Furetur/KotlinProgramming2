package homeworks.hw3.server

import homeworks.hw3.simulation.CarEventType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal abstract class ParkingTest {
    abstract fun handleEvents(parkingSystem: ParkingSystem, events: List<CarEventType>)

    fun handleEvent(parkingSystem: ParkingSystem, event: CarEventType) {
        when(event) {
            CarEventType.ARRIVAL -> parkingSystem.acceptCar()
            CarEventType.DEPARTURE -> parkingSystem.handleCarDeparture()
        }
    }

    private fun populateParking(parkingSystem: ParkingSystem, carsCount: Int = parkingSystem.getEmptySpacesCount()) {
        val events = List(carsCount) { CarEventType.ARRIVAL }
        handleEvents(parkingSystem, events)
    }

    private fun freeParking(parkingSystem: ParkingSystem, carsCount: Int = parkingSystem.getOccupiedSpacesCount()) {
        val events = List(carsCount) { CarEventType.DEPARTURE }
        handleEvents(parkingSystem, events)
    }

    @Test
    fun `should throw if parking has 0 spaces in total and a car tries to arrive`() {
        val parkingSystem = ParkingSystem(0)
        val events = listOfNotNull(CarEventType.ARRIVAL)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            handleEvents(parkingSystem, events)
        }
    }

    @Test
    fun `should throw if small parking is populated and new car tries to park`() {
        val parkingSystem = ParkingSystem(10)
        populateParking(parkingSystem)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            populateParking(parkingSystem, 1)
        }
    }

    @Test
    fun `should throw if large parking is populated and new car tries to park`() {
        val parkingSystem = ParkingSystem(1000)
        populateParking(parkingSystem)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            populateParking(parkingSystem, 1)
        }
    }

    @Test
    fun `emptySpacesCount should be max initially`() {
        val parkingSystem = ParkingSystem(1000)
        assertEquals(1000, parkingSystem.getEmptySpacesCount())
    }

    @Test
    fun `emptySpacesCount should be 50 when 100 spaces parking has 50 parked cars`() {
        val parkingSystem = ParkingSystem(100)
        populateParking(parkingSystem, 50)
        assertEquals(50, parkingSystem.getEmptySpacesCount())
    }

    @Test
    fun `emptySpacesCount should be 100 when 1000 spaces parking has 900 parked cars`() {
        val parkingSystem = ParkingSystem(1000)
        populateParking(parkingSystem, 900)
        assertEquals(100, parkingSystem.getEmptySpacesCount())
    }

    @Test
    fun `emptySpacesCount should be 1 if 1 car departs from full parking`() {
        val parkingSystem = ParkingSystem(1000)
        populateParking(parkingSystem)
        freeParking(parkingSystem, 1)
        assertEquals(1, parkingSystem.getEmptySpacesCount())
    }

    @Test
    fun `emptySpacesCount should be 500 if 500 car departs from full parking`() {
        val parkingSystem = ParkingSystem(1000)
        populateParking(parkingSystem)
        freeParking(parkingSystem, 500)
        assertEquals(500, parkingSystem.getEmptySpacesCount())
    }
}
