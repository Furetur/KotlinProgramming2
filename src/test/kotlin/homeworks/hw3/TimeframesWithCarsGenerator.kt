package homeworks.hw3

import homeworks.hw3.simulation.ParkingSimulation

object TimeframesWithCarsGenerator {
    data class TimeframesWithCars(val framesEvents: List<Set<ParkingSimulation.CarEvent>>, val cars: List<Car>)

    private fun generateCarLayer(time: Int, carsCount: Int, willEveryCarLeave: Boolean, gateId: Int): List<Car> =
        List(carsCount) {
            if (willEveryCarLeave) {
                Car(time, gateId, time + 1, gateId)
            } else {
                Car(time, gateId)
            }
        }

    fun generateTimeframesWithCars(framesCount: Int, carsCount: Int, carsOnceDepart: Boolean): TimeframesWithCars {
        var prevCars = generateCarLayer(0, carsCount, true, 0)
        val allCars = prevCars.toMutableList()
        val startingFrameEvents = prevCars.map { EventsGenerator.makeArrivalEvent(it) }.toSet()
        val allFramesEvents = mutableListOf(startingFrameEvents)
        for (i in 1 until framesCount) {
            val currentCarLayer = generateCarLayer(i, carsCount, carsOnceDepart && i != framesCount - 1, i)
            val currentFrameEvents =
                (currentCarLayer.map { EventsGenerator.makeArrivalEvent(it) } + prevCars.mapNotNull {
                    EventsGenerator.makeDepartureEvent(it)
                }).toSet()
            allFramesEvents.add(currentFrameEvents)
            allCars.addAll(currentCarLayer)
            prevCars = currentCarLayer
        }
        return TimeframesWithCars(allFramesEvents, allCars)
    }

    fun generateSimpleTimeframesWithCars(framesCount: Int, carsDepart: Boolean): TimeframesWithCars =
        generateTimeframesWithCars(framesCount, 1, carsDepart)

}
