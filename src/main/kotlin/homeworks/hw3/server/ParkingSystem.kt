package homeworks.hw3.server

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicInteger

/**
 * The main class that is responsible for the server logic
 * Can be called from multiple threads
 * Should be installed on the server side
 */
class ParkingSystem(private val parkingSpacesCount: Int) {
    private val emptySpacesCount = AtomicInteger(parkingSpacesCount)

    init {
        if (parkingSpacesCount < 0) {
            throw NegativeParkingSpacesException()
        }
    }

    // this method should be called from multiple threads
    fun getEmptySpacesCount() = emptySpacesCount.get()

    fun getOccupiedSpacesCount() = parkingSpacesCount - getEmptySpacesCount()

    // this method should be called from multiple threads
    fun acceptCar() {
        val oldValue = emptySpacesCount.getAndUpdate { oldValue -> if (oldValue > 0) oldValue - 1 else 0 }
        if (oldValue == 0) {
            throw NoFreeParkingSpacesException()
        }
    }

    // this method should be called from multiple threads
    fun handleCarDeparture() {
        emptySpacesCount.getAndUpdate { oldValue ->
            if (oldValue < parkingSpacesCount) {
                oldValue + 1
            } else parkingSpacesCount
        }
    }

    class NegativeParkingSpacesException : IllegalArgumentException("parkingSpacesCount cannot be negative")

    class NoFreeParkingSpacesException : IllegalStateException("No free parking spaces")
}
