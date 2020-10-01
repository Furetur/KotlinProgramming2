package homeworks.hw3.server

import homeworks.hw3.Car
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class ParkingSystem(private val parkingSpacesCount: Int) {
    private val parkedCars = mutableSetOf<Car>()

    val occupiedSpacesCount: Int
        get() = parkedCars.size

    val emptySpacesCount: Int
        get() = parkingSpacesCount - occupiedSpacesCount

    init {
        if (parkingSpacesCount < 0) {
            throw NegativeParkingSpacesException()
        }
    }

    fun acceptCar(car: Car) {
        if (emptySpacesCount == 0) {
            throw NoFreeParkingSpacesException()
        }
        if (parkedCars.contains(car)) {
            throw CarIsAlreadyParkedException()
        }
        parkedCars.add(car)
    }

    fun handleCarDeparture(car: Car) {
        if (!parkedCars.contains(car)) {
            throw CarWasNotParkedException()
        }
        parkedCars.remove(car)
    }

    class NegativeParkingSpacesException : IllegalArgumentException("parkingSpacesCount cannot be negative")

    class NoFreeParkingSpacesException : IllegalStateException("No free parking spaces")

    class CarWasNotParkedException : IllegalArgumentException("Car that was not parked tried to depart")

    class CarIsAlreadyParkedException : IllegalArgumentException("Car tried to park twice")
}
