package homeworks.hw3

import homeworks.hw3.server.ServerResponse

/**
 * In a real world app classes that implement this interface should allow the gates to send data to the server
 */
interface ClientNetworkApi {
    /**
     * Should send data to the server via the internet
     */
    fun handleCarArrival(): ServerResponse
    /**
     * Should send data to the server via the internet
     */
    fun handleCarDeparture(): ServerResponse
}
