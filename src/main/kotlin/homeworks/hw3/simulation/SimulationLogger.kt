package homeworks.hw3.simulation

class SimulationLogger(private val parkingSpacesCount: Int) {
    fun logEvent(event: ParkingSimulation.CarEvent) {
        println(
            when (event.type) {
                ParkingSimulation.CarEventType.ARRIVAL ->
                    "\tCar entered the parking through the gate id=${event.gateId}"
                ParkingSimulation.CarEventType.DEPARTURE ->
                    "\tCar left the parking through the gate id=${event.gateId}"
            }
        )
    }

    fun logTimeframeStart(time: Int) {
        println("START Timeframe time=$time")
    }

    fun logTimeframeEnd(emptySpacesCount: Int) {
        println("END")
        println("> There are $emptySpacesCount/$parkingSpacesCount available parking spaces")
    }
}
