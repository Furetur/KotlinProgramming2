package homeworks.hw3

import homeworks.hw3.server.ParkingSystem
import homeworks.hw3.server.ServerResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

/**
 * This is a mock class that simulates the interaction between the gates and the server
 * Should be on the *server* side
 * Should listen for incoming messages from clients
 * And send messages back to clients
 *
 * For now it is used by gates.
 * It blocks the client's thread until the server response is received
 */
class ServerApi(private val system: ParkingSystem) : ClientNetworkApi {
    override fun handleCarArrival(): ServerResponse = runBlocking {
        val result = async { getArrivalResponseFromServer() }
        return@runBlocking result.await()
    }

    override fun handleCarDeparture(): ServerResponse = runBlocking {
        val result = async { system.handleCarDeparture() }
        result.await()
        return@runBlocking ServerResponse.SUCCESS
    }

    fun getEmptySpacesCount() = runBlocking {
        system.getEmptySpacesCount()
    }

    private fun getArrivalResponseFromServer() = try {
        system.acceptCar()
        ServerResponse.SUCCESS
    } catch (e: ParkingSystem.NoFreeParkingSpacesException) {
        ServerResponse.NO_FREE_SPACES
    }
}
