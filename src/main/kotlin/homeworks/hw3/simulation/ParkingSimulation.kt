package homeworks.hw3.simulation

import homeworks.hw3.Gate
import homeworks.hw3.ServerApi
import homeworks.hw3.server.ParkingSystem
import homeworks.hw3.server.ServerResponse
import java.lang.IllegalArgumentException

open class ParkingSimulation(config: ParkingSimulationConfig) {
    private val parkingSpacesCount = config.parkingSpacesCount
    private val gatesCount = config.gatesCount
    private val cars = config.cars

    init {
        if (gatesCount <= 0) {
            throw NegativeGatesCountException()
        }
        validateCars()
    }

    private val logger = SimulationLogger(parkingSpacesCount)

    protected val frames = makeFrames()
    private val system = ParkingSystem(parkingSpacesCount)
    private val serverApi = ServerApi(system)

    private val gates = List(gatesCount) { Gate(serverApi) }

    private fun isGateDefined(gateId: Int): Boolean = gateId in 0 until gatesCount

    private fun validateCars() {
        for (car in cars) {
            if (!isGateDefined(car.arrivalGateId)) {
                throw UnknownGateException(car.arrivalGateId)
            }
            val departureGateId = car.departureGateId
            if (departureGateId != null && !isGateDefined(departureGateId)) {
                throw UnknownGateException(departureGateId)
            }
        }
    }

    private fun makeFrames(): List<Timeframe> {
        val arrivalEvents = cars.map { CarEvent.getCarArrivalEvent(it) }
        val departureEvents = cars.mapNotNull { CarEvent.getCarDepartureEvent(it) }
        val events = arrivalEvents + departureEvents
        val eventsGroupedByTime = mutableMapOf<Int, MutableSet<CarEvent>>()
        for (event in events) {
            val currentEvents = eventsGroupedByTime[event.time]
            if (currentEvents == null) {
                eventsGroupedByTime[event.time] = mutableSetOf(event)
            } else {
                currentEvents.add(event)
            }
        }
        return eventsGroupedByTime.entries.map { Timeframe(it.key, it.value) }.sortedBy { it.time }
    }

    fun run() {
        for (frame in frames) {
            logger.logTimeframeStart(frame.time)
            frame.run()
            logger.logTimeframeEnd(serverApi.getEmptySpacesCount())
        }
    }

    inner class Timeframe(val time: Int, val events: Set<CarEvent>) {
        fun run() {
            for (event in events) {
                val response = passEventToGate(event)
                logger.logEvent(event, response)
            }
        }

        private fun passEventToGate(event: CarEvent): ServerResponse {
            val gate = if (event.gateId in gates.indices) {
                gates[event.gateId]
            } else {
                throw UnknownGateException(event.gateId)
            }
            return when (event.type) {
                CarEventType.ARRIVAL -> gate.acceptNewCar()
                CarEventType.DEPARTURE -> gate.handleCarDeparture()
            }
        }
    }

    class NegativeGatesCountException : IllegalArgumentException("gatesCount must be positive")

    class UnknownGateException(gateId: Int) : IllegalArgumentException("Gate with id=$gateId is not defined")
}
