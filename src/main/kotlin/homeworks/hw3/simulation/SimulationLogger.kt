package homeworks.hw3.simulation

import homeworks.hw3.server.ServerResponse

class SimulationLogger(private val parkingSpacesCount: Int) {
    fun logEvent(event: CarEvent, response: ServerResponse) {
        when (response) {
            ServerResponse.SUCCESS -> logSuccessfulEvent(event)
            ServerResponse.NO_FREE_SPACES -> logFailedEvent()
        }
    }

    private fun logSuccessfulEvent(event: CarEvent) {
        println(
            when (event.type) {
                CarEventType.ARRIVAL ->
                    "\tCar entered the parking through the gate id=${event.gateId}"
                CarEventType.DEPARTURE ->
                    "\tCar left the parking through the gate id=${event.gateId}"
            }
        )
    }

    private fun logFailedEvent() {
        println(
            "\tCar tried to use the parking but there were no available parking spaces." +
                    "The car was ignored"
        )
    }

    fun logTimeframeStart(time: Int) {
        println("START Timeframe time=$time")
    }

    fun logTimeframeEnd(emptySpacesCount: Int) {
        println("END.")
        println("There are $emptySpacesCount/$parkingSpacesCount free parking spaces")
    }
}
