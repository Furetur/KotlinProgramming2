package homeworks.hw3.simulation

import homeworks.hw3.Car
import homeworks.hw3.EventsGenerator
import homeworks.hw3.TimeframesWithCarsGenerator
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ParkingSimulationTest {
    private fun testOnSimpleFlatCarHistory(size: Int, carsDepart: Boolean) {
        val (frames, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(size, carsDepart)
        val config = ParkingSimulationConfig(size, 10, cars)
        val timeline = TestableParkingSimulation(config)
        assertEquals(frames, timeline.timeframesEvents)
    }

    private fun testOnSmallWideCarHistory(length: Int, width: Int, carsDepart: Boolean) {
        val (frames, cars) = TimeframesWithCarsGenerator.generateTimeframesWithCars(length, width, carsDepart)
        val config = ParkingSimulationConfig(length, 10, cars)
        val timeline = TestableParkingSimulation(config)
        assertEquals(frames, timeline.timeframesEvents)
    }

    private fun makeArrivalEvent(car: Car): ParkingSimulation.CarEvent
            = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.ARRIVAL, car)

    private fun makeDepartureEvent(car: Car): ParkingSimulation.CarEvent
            = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.DEPARTURE, car)

    private val car = Car(0, 0, 1, 2)
    private val carThatNeverDeparts = Car(0, 0)


    // Constructor tests

    @Test
    fun `should throw if gateCount is negative`() {
        val car = Car(0, 500, 1, 500)
        val config = ParkingSimulationConfig(-1, 5, listOf(car))
        assertThrows(ParkingSimulation.NotPositiveGatesCountException::class.java) {
            ParkingSimulation(config)
        }
    }

    @Test
    fun `should throw if gateCount is 0`() {
        val car = Car(0, 500, 1, 500)
        val config = ParkingSimulationConfig(0, 5, listOf(car))
        assertThrows(ParkingSimulation.NotPositiveGatesCountException::class.java) {
            ParkingSimulation(config)
        }
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should throw if car enters through an unknown gate`() {
        val car = Car(0, 500, 1, 500)
        val config = ParkingSimulationConfig(1, 5, listOf(car))
        assertThrows(ParkingSimulation.UnknownGateException::class.java) {
            ParkingSimulation(config)
        }
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should throw if car leaves through an unknown gate`() {
        val car = Car(0, 0, 1, 500)
        val config = ParkingSimulationConfig(1, 5, listOf(car))
        assertThrows(ParkingSimulation.UnknownGateException::class.java) {
            ParkingSimulation(config)
        }
    }

    // run tests

    @ObsoleteCoroutinesApi
    @Test
    fun `should run without exceptions`() {
        val (_, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(10, false)
        val config = ParkingSimulationConfig(10, 5, cars)
        val simulation = ParkingSimulation(config)
        simulation.run()
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should run large without exceptions`() {
        val (_, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(1000, false)
        val config = ParkingSimulationConfig(1000, 500, cars)
        val simulation = ParkingSimulation(config)
        simulation.run()
    }

    // CarEvent Tests

    @Test
    fun `arrival CarEvent should set time to arrivalTime when given a car that departs once`() {
        val event = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.ARRIVAL, car)
        assertEquals(car.arrivalTime, event.time)
    }

    @Test
    fun `arrival CarEvent should set time to arrivalTime when given a car that never departs`() {
        val event = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.ARRIVAL, carThatNeverDeparts)
        assertEquals(carThatNeverDeparts.arrivalTime, event.time)
    }

    @Test
    fun `arrival CarEvent should set gateId when given a car that departs once`() {
        val event = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.ARRIVAL, car)
        assertEquals(car.arrivalGateId, event.gateId)
    }

    @Test
    fun `arrival CarEvent should set gateId when given a car that never departs`() {
        val event = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.ARRIVAL, carThatNeverDeparts)
        assertEquals(carThatNeverDeparts.arrivalGateId, event.gateId)
    }

    @Test
    fun `departure CarEvent should set time`() {
        val event = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.DEPARTURE, car)
        assertEquals(car.departureTime, event.time)
    }

    @Test
    fun `departure CarEvent should set gateId`() {
        val event = ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.DEPARTURE, car)
        assertEquals(car.departureGateId, event.gateId)
    }

    @Test
    fun `departure CarEvent should throw if a car never departs`() {
        assertThrows(ParkingSimulation.CarDoesNotDepartException::class.java) {
            ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.DEPARTURE, carThatNeverDeparts)
        }
    }

    // Timeline tests

    @Test
    fun `should correctly construct 1 frame from 1 car`() {
        val car = Car(0, 0)
        val arrival = EventsGenerator.makeArrivalEvent(car)
        val expectedEvents = listOf(setOf(arrival))
        val config = ParkingSimulationConfig(1, 1, listOf(car))
        val timeline = TestableParkingSimulation(config)
        assertEquals(expectedEvents, timeline.timeframesEvents)
    }
    @Test
    fun `should correctly construct 2 frames from 1 car`() {
        val car = Car(0, 0, 1, 0)
        val arrival = EventsGenerator.makeArrivalEvent(car)
        val departure = EventsGenerator.makeDepartureEvent(car)
        val expectedFramesEvents = listOf(setOf(arrival), listOfNotNull(departure).toSet())
        val config = ParkingSimulationConfig(1, 1, listOf(car))
        val timeline = TestableParkingSimulation(config)
        assertEquals(expectedFramesEvents, timeline.timeframesEvents)
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

    @Test
    fun `CarEvent should throw if user attemps to create a depart event from car that never departs`() {
        assertThrows(ParkingSimulation.CarDoesNotDepartException::class.java) {
            ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.DEPARTURE, carThatNeverDeparts)
        }
    }
}
