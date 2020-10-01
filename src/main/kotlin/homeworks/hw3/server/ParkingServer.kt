package homeworks.hw3.server

import homeworks.hw3.Car
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

class ParkingServer(parkingSpacesCount: Int) {
    private val parkingSystem = ParkingSystem(parkingSpacesCount)

    val emptySpacesCount: Int
        get() = parkingSystem.emptySpacesCount

    val occupiedSpacesCount: Int
        get() = parkingSystem.occupiedSpacesCount

    @ObsoleteCoroutinesApi
    private val serverSingleThreadContext = newSingleThreadContext("ParkingServerSingleThreadContext")

    @ObsoleteCoroutinesApi
    suspend fun acceptCar(car: Car) {
        withContext(serverSingleThreadContext) {
            parkingSystem.acceptCar(car)
        }
    }

    @ObsoleteCoroutinesApi
    suspend fun handleCarDeparture(car: Car) {
        withContext(serverSingleThreadContext) {
            parkingSystem.handleCarDeparture(car)
        }
    }
}
