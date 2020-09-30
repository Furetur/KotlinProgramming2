package homeworks.hw3

object TimeframesWithCarsGenerator {
    data class TimeframesWithCars(val frames: List<Timeline.Timeframe>, val cars: List<Car>)

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
        val startingFrame = Timeline.Timeframe(0, prevCars.map { it.arrival }.toSet())
        val allFrames = mutableListOf(startingFrame)
        for (i in 1 until framesCount) {
            val currentCarLayer = generateCarLayer(i, carsCount, carsOnceDepart && i != framesCount - 1, i)
            val currentFrameEvents = (currentCarLayer.map { it.arrival } + prevCars.mapNotNull { it.departure }).toSet()
            val currentFrame = Timeline.Timeframe(i, currentFrameEvents)
            allFrames.add(currentFrame)
            allCars.addAll(currentCarLayer)
            prevCars = currentCarLayer
        }
        return TimeframesWithCars(allFrames, allCars)
    }

    fun generateSimpleTimeframesWithCars(framesCount: Int, carsDepart: Boolean): TimeframesWithCars =
        generateTimeframesWithCars(framesCount, 1, carsDepart)

}
