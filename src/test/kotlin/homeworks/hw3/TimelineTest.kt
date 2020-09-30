package homeworks.hw3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TimelineTest {

    private fun testOnSimpleFlatCarHistory(size: Int, carsDepart: Boolean) {
        val (frames, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(size, carsDepart)
        val timeline = Timeline(cars)
        assertEquals(frames, timeline.frames)
    }

    private fun testOnSmallWideCarHistory(length: Int, width: Int, carsDepart: Boolean) {
        val (frames, cars) = TimeframesWithCarsGenerator.generateTimeframesWithCars(length, width, carsDepart)
        val timeline = Timeline(cars)
        assertEquals(frames, timeline.frames)
    }

    private val car = Car(0, 0, 1, 2)
    val carThatNeverDeparts = Car(0, 0)

    // CarEvent Tests

    @Test
    fun `arrival CarEvent should set time to arrivalTime when given a car that departs once`() {
        val event = Timeline.CarEvent(Timeline.CarEventType.ARRIVAL, car)
        assertEquals(car.arrivalTime, event.time)
    }

    @Test
    fun `arrival CarEvent should set time to arrivalTime when given a car that never departs`() {
        val event = Timeline.CarEvent(Timeline.CarEventType.ARRIVAL, carThatNeverDeparts)
        assertEquals(carThatNeverDeparts.arrivalTime, event.time)
    }

    @Test
    fun `arrival CarEvent should set gateId when given a car that departs once`() {
        val event = Timeline.CarEvent(Timeline.CarEventType.ARRIVAL, car)
        assertEquals(car.arrivalGateId, event.gateId)
    }

    @Test
    fun `arrival CarEvent should set gateId when given a car that never departs`() {
        val event = Timeline.CarEvent(Timeline.CarEventType.ARRIVAL, carThatNeverDeparts)
        assertEquals(carThatNeverDeparts.arrivalGateId, event.gateId)
    }

    @Test
    fun `departure CarEvent should set time`() {
        val event = Timeline.CarEvent(Timeline.CarEventType.DEPARTURE, car)
        assertEquals(car.departureTime, event.time)
    }

    @Test
    fun `departure CarEvent should set gateId`() {
        val event = Timeline.CarEvent(Timeline.CarEventType.DEPARTURE, car)
        assertEquals(car.departureGateId, event.gateId)
    }

    @Test
    fun `departure CarEvent should throw if a car never departs`() {
        assertThrows(Timeline.CarDoesNotDepartException::class.java) {
            Timeline.CarEvent(Timeline.CarEventType.DEPARTURE, carThatNeverDeparts)
        }
    }

    // Timeline tests
    @Test
    fun `should correctly construct 1 frame from 1 car`() {
        val car = Car(0, 0)
        val expectedFrames = listOf(
            Timeline.Timeframe(0, setOf(car.arrival))
        )
        val timeline = Timeline(listOf(car))
        assertEquals(expectedFrames, timeline.frames)
    }
    @Test
    fun `should correctly construct 2 frames from 1 car`() {
        val car = Car(0, 0, 1, 0)
        val expectedFrames = listOf(
            Timeline.Timeframe(0, setOf(car.arrival)),
            Timeline.Timeframe(1, listOfNotNull(car.departure).toSet())
        )
        val timeline = Timeline(listOf(car))
        assertEquals(expectedFrames, timeline.frames)
    }
    @Test
    fun `should correctly construct frames from few simple cars history`() {
        testOnSimpleFlatCarHistory(2, false)
    }
    @Test
    fun `should correctly construct frames from few simple cars history with depart`() {
        testOnSimpleFlatCarHistory(2, true)
    }
    @Test
    fun `should correctly construct frames from medium number of simple cars`() {
        testOnSimpleFlatCarHistory(10, false)
    }
    @Test
    fun `should correctly construct frames from medium number of simple cars that depart`() {
        testOnSimpleFlatCarHistory(10, true)
    }
    @Test
    fun `should correctly construct frames from large number of simple cars`() {
        testOnSimpleFlatCarHistory(100, false)
    }
    @Test
    fun `should correctly construct frames from large number of simple cars that depart`() {
        testOnSimpleFlatCarHistory(100, true)
    }

    @Test
    fun `should correctly construct frames from small but wide car history`() {
        testOnSmallWideCarHistory(2, 5, true)
    }

    @Test
    fun `should correctly construct frames from medium wide car history`() {
        testOnSmallWideCarHistory(10, 10, true)
    }

    @Test
    fun `should correctly construct frames from large wide car history`() {
        testOnSmallWideCarHistory(100, 50, true)
    }
}
