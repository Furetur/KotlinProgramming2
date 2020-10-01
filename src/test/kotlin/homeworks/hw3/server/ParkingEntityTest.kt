package homeworks.hw3.server

import homeworks.hw3.Car
import homeworks.hw3.EventsGenerator
import homeworks.hw3.simulation.ParkingSimulation
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal abstract class ParkingEntityTest<Entity> {
    abstract fun handleEvents(parkingSystem: Entity, events: List<ParkingSimulation.CarEvent>)

    abstract fun getEmptySpacesCount(parkingSystem: Entity): Int

    abstract fun getOccupiedSpacesCount(parkingSystem: Entity): Int

    abstract fun createEntity(parkingSpacesCount: Int): Entity

    private fun populateParking(
        parkingSystem: Entity,
        carsCount: Int = getEmptySpacesCount(parkingSystem),
    ): List<Car> {
        val allCars = List(carsCount) { Car(0, 0, 1, 0) }
        val events = allCars.map { EventsGenerator.makeArrivalEvent(it) }
        handleEvents(parkingSystem, events)
        return allCars
    }

    private fun freeParking(
        parkingSystem: Entity,
        parkedCars: List<Car>,
        carsCount: Int = getOccupiedSpacesCount(parkingSystem)
    ): List<Car> {
        val events = parkedCars.slice(0 until carsCount).mapNotNull { EventsGenerator.makeDepartureEvent(it) }
        handleEvents(parkingSystem, events)
        return parkedCars.slice(carsCount until parkedCars.size)
    }

    @Test
    fun `should throw if car that has never arrived tries to depart`() {
        val car = Car(0, 500, 1, 0)
        val parkingSystem = createEntity(1)
        val event = EventsGenerator.makeDepartureEvent(car)
        val events = listOfNotNull(event)
        assertThrows(ParkingSystem.CarWasNotParkedException::class.java) {
            handleEvents(parkingSystem, events)
        }
    }

    @Test
    fun `should throw if car arrives twice`() {
        val car = Car(0, 500, 1, 0)
        val parkingSystem = createEntity(2)
        val arrival1 = EventsGenerator.makeArrivalEvent(car)
        val arrival2 = EventsGenerator.makeArrivalEvent(car)
        val events = listOf(arrival1, arrival2)
        assertThrows(ParkingSystem.CarIsAlreadyParkedException::class.java) {
            handleEvents(parkingSystem, events)
        }
    }

    @Test
    fun `should throw if parking has 0 spaces in total and a car tries to arrive`() {
        val car = Car(0, 0, 1, 1)
        val parkingSystem = createEntity(0)
        val event = EventsGenerator.makeArrivalEvent(car)
        val events = listOfNotNull(event)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            handleEvents(parkingSystem, events)
        }
    }

    @Test
    fun `should throw if small parking is populated and new car tries to park`() {
        val parkingSystem = createEntity(10)
        populateParking(parkingSystem)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            populateParking(parkingSystem, 1)
        }
    }

    @Test
    fun `should throw if large parking is populated and new car tries to park`() {
        val parkingSystem = createEntity(1000)
        populateParking(parkingSystem)
        assertThrows(ParkingSystem.NoFreeParkingSpacesException::class.java) {
            populateParking(parkingSystem, 1)
        }
    }

    @Test
    fun `emptySpacesCount should be max initially`() {
        val parkingSystem = createEntity(1000)
        assertEquals(1000, getEmptySpacesCount(parkingSystem))
    }

    @Test
    fun `emptySpacesCount should be 50 when 100 spaces parking has 50 parked cars`() {
        val parkingSystem = createEntity(100)
        populateParking(parkingSystem, 50)
        assertEquals(50, getEmptySpacesCount(parkingSystem))
    }

    @Test
    fun `emptySpacesCount should be 100 when 1000 spaces parking has 900 parked cars`() {
        val parkingSystem = createEntity(1000)
        populateParking(parkingSystem, 900)
        assertEquals(100, getEmptySpacesCount(parkingSystem))
    }

    @Test
    fun `emptySpacesCount should be 1 if 1 car departs from full parking`() {
        val parkingSystem = createEntity(1000)
        val parkedCars = populateParking(parkingSystem)
        freeParking(parkingSystem, parkedCars, 1)
        assertEquals(1, getEmptySpacesCount(parkingSystem))
    }

    @Test
    fun `emptySpacesCount should be 500 if 500 car departs from full parking`() {
        val parkingSystem = createEntity(1000)
        val parkedCars = populateParking(parkingSystem)
        freeParking(parkingSystem, parkedCars, 500)
        assertEquals(500, getEmptySpacesCount(parkingSystem))
    }
}
