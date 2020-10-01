package homeworks.hw3.simulation

class TestableParkingSimulation(config: ParkingSimulationConfig) : ParkingSimulation(config) {
    val timeframesEvents: List<Set<CarEvent>>
        get() = frames.map { it.events }
}
