package homeworks.hw3

/**
 * This is the class that should be installed on the actual parking gate
 * Should receive ClientNetworkApi instance that sends messages to the server
 */
class Gate(private val api: ClientNetworkApi) {
    fun acceptNewCar() = api.handleCarArrival()

    fun handleCarDeparture() = api.handleCarDeparture()
}
