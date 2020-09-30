package homeworks.hw3.server

import homeworks.hw3.Car
import homeworks.hw3.Timeline
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.Stack

class ParkingSystem(private val parkingSpacesCount: Int) : EventsHandler {
    private val emptySpaces = Stack<Int>()
    private val carsToSpaces = mutableMapOf<Car, Int>()

    init {
        for (i in 0 until parkingSpacesCount) {
            emptySpaces.push(i)
        }
    }

    val emptySpacesCount: Int
        get() = emptySpaces.size

    val occupiedSpacesCount: Int
        get() = parkingSpacesCount - emptySpacesCount

    override fun handleEvent(event: Timeline.CarEvent) {
        when (event.type) {
            Timeline.CarEventType.ARRIVAL -> acceptNewCar(event.car)
            Timeline.CarEventType.DEPARTURE -> handleCarDeparture(event.car)
        }
    }

    private fun acceptNewCar(car: Car) {
        if (emptySpacesCount == 0) {
            throw NoFreeParkingSpacesException()
        }
        val emptySpace = emptySpaces.pop() ?: throw NoFreeParkingSpacesException()
        carsToSpaces[car] = emptySpace
    }

    private fun handleCarDeparture(car: Car) {
        val carSpace = carsToSpaces[car] ?: throw CarWasNotParkedException()
        emptySpaces.push(carSpace)
    }

    class NoFreeParkingSpacesException : IllegalStateException()

    class CarWasNotParkedException : IllegalArgumentException()
}
