package homeworks.hw3

import java.lang.IllegalArgumentException

data class Timeline(val cars: List<Car>) {

    var frames: List<Timeframe>

    init {
        val events = cars.map { it.arrival } + cars.mapNotNull { it.departure }
        val eventsGroupedByTime = mutableMapOf<Int, MutableSet<CarEvent>>()
        for (event in events) {
            val currentEvents = eventsGroupedByTime[event.time]
            if (currentEvents == null) {
                eventsGroupedByTime[event.time] = mutableSetOf(event)
            } else {
                currentEvents.add(event)
            }
        }
        frames = eventsGroupedByTime.entries.map { Timeframe(it.key, it.value) }.sortedBy { it.time }
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

    data class Timeframe(val time: Int, val events: Set<CarEvent>)
}
