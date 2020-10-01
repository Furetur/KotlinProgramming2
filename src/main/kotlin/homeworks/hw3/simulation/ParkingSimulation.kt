package homeworks.hw3.simulation

import homeworks.hw3.Car
import homeworks.hw3.server.ParkingServer
import homeworks.hw3.server.ParkingSystem
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

open class ParkingSimulation(config: ParkingSimulationConfig) {
    private val parkingSpacesCount = config.parkingSpacesCount
    private val gatesCount = config.gatesCount
    private val cars = config.cars

    private val logger = SimulationLogger(parkingSpacesCount)
    protected val frames = makeFrames()

    private val server = ParkingServer(parkingSpacesCount)

    init {
        if (gatesCount <= 0) {
            throw NotPositiveGatesCountException()
        }
        validateCars()
    }

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
        val arrivalEvents = cars.map { CarEvent(CarEventType.ARRIVAL, it) }
        val departureEvents = cars.filter { it.departureTime != null }.map {
            CarEvent(
                CarEventType.DEPARTURE,
                it
            )
        }
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

    @ObsoleteCoroutinesApi
    fun run() {
        for (frame in frames) {
            logger.logTimeframeStart(frame.time)
            frame.run()
            logger.logTimeframeEnd(server.emptySpacesCount)
        }
    }

    @ObsoleteCoroutinesApi
    private suspend fun handleEvent(event: CarEvent) {
        when (event.type) {
            CarEventType.ARRIVAL -> server.acceptCar(event.car)
            CarEventType.DEPARTURE -> server.handleCarDeparture(event.car)
        }
    }

    enum class CarEventType {
        ARRIVAL,
        DEPARTURE
    }

    data class CarEvent(val type: CarEventType, val car: Car) {
        val time: Int = when (type) {
            CarEventType.ARRIVAL -> car.arrivalTime
            CarEventType.DEPARTURE -> car.departureTime ?: throw CarDoesNotDepartException(car)
        }
        val gateId = when (type) {
            CarEventType.ARRIVAL -> car.arrivalGateId
            CarEventType.DEPARTURE -> car.departureGateId ?: car.arrivalGateId
        }
    }

    class CarDoesNotDepartException(car: Car) : IllegalArgumentException(
        "Tried to create a departure event for car which arrives at time=${car.arrivalTime} " +
                "at gate=${car.arrivalGateId} but does not departure"
    )

    inner class Timeframe(val time: Int, val events: Set<CarEvent>) {
        @ObsoleteCoroutinesApi
        fun run() = runBlocking {
            for (event in events) {
                launch {
                    try {
                        handleEvent(event)
                        logger.logEvent(event)
                    } catch (e: ParkingSystem.NoFreeParkingSpacesException) {
                        println(
                            "\tCar tried to use the parking but there were no available parking spaces." +
                                    "The car was ignored"
                        )
                    }
                }
            }
        }
    }

    class NotPositiveGatesCountException : IllegalArgumentException("gatesCount must be positive")

    class UnknownGateException(gateId: Int) : IllegalArgumentException("Gate with id=$gateId is not defined")
}
