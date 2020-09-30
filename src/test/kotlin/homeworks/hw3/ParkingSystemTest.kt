package homeworks.hw3

import homeworks.hw3.server.ParkingSystem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ParkingSystemTest {

    private fun populateParking(
        parkingSystem: ParkingSystem,
        carsCount: Int = parkingSystem.emptySpacesCount,
    ): List<Car> {
        val allCars = mutableListOf<Car>()
        for (i in 0 until carsCount) {
            val car = Car(0, 0, 1, 0)
            allCars.add(car)
            parkingSystem.handleEvent(car.arrival)
        }
        return allCars
    }

    private fun freeParking(
        parkingSystem: ParkingSystem,
        parkedCars: List<Car>,
        carsCount: Int = parkingSystem.occupiedSpacesCount
    ): List<Car> {
        for (car in parkedCars.slice(0 until carsCount)) {
            val departure = car.departure
            if (departure != null) {
                parkingSystem.handleEvent(departure)
            }
        }
        return parkedCars.slice(carsCount until parkedCars.size)
    }

    @Test
    fun `should throw if car that has never arrived tries to depart`() {
        val car = Car(0, 500, 1, 0)
        val parkingSystem = ParkingSystem(0)
        assertThrows(ParkingSystem.CarWasNotParkedException::class.java) {
            parkingSystem.handleEvent(car.departure!!)
        }
    }

    @Test
    fun `should throw if parking has 0 spaces in total and a car tries to arrive`() {
        val car = Car(0, 0, 1, 1)
        val parkingSystem = ParkingSystem(0)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            parkingSystem.handleEvent(car.arrival)
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
        assertEquals(1000, parkingSystem.emptySpacesCount)
    }

    @Test
    fun `emptySpacesCount should be 50 when 100 spaces parking has 50 parked cars`() {
        val parkingSystem = ParkingSystem(100)
        populateParking(parkingSystem, 50)
        assertEquals(50, parkingSystem.emptySpacesCount)
    }

    @Test
    fun `emptySpacesCount should be 100 when 1000 spaces parking has 900 parked cars`() {
        val parkingSystem = ParkingSystem(1000)
        populateParking(parkingSystem, 900)
        assertEquals(100, parkingSystem.emptySpacesCount)
    }

    @Test
    fun `emptySpacesCount should be 1 if 1 car departs from full parking`() {
        val parkingSystem = ParkingSystem(1000)
        val parkedCars = populateParking(parkingSystem)
        freeParking(parkingSystem, parkedCars, 1)
        assertEquals(1, parkingSystem.emptySpacesCount)
    }

    @Test
    fun `emptySpacesCount should be 500 if 500 car departs from full parking`() {
        val parkingSystem = ParkingSystem(1000)
        val parkedCars = populateParking(parkingSystem)
        freeParking(parkingSystem, parkedCars, 500)
        assertEquals(500, parkingSystem.emptySpacesCount)
    }
}
