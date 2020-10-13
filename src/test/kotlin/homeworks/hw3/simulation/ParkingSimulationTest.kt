package homeworks.hw3.simulation

import homeworks.hw3.*
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

    private val car = Car(0, 0, 1, 2)


    // Constructor tests

    @Test
    fun `should throw if gateCount is negative`() {
        val car = Car(0, 500, 1, 500)
        val config = ParkingSimulationConfig(-1, 5, listOf(car))
        assertThrows(ParkingSimulation.NegativeGatesCountException::class.java) {
            ParkingSimulation(config)
        }
    }

    @Test
    fun `should throw if gateCount is 0`() {
        val car = Car(0, 500, 1, 500)
        val config = ParkingSimulationConfig(0, 5, listOf(car))
        assertThrows(ParkingSimulation.NegativeGatesCountException::class.java) {
            ParkingSimulation(config)
        }
    }

    @Test
    fun `should throw if car enters through an unknown gate`() {
        val car = Car(0, 500, 1, 500)
        val config = ParkingSimulationConfig(1, 5, listOf(car))
        assertThrows(ParkingSimulation.UnknownGateException::class.java) {
            ParkingSimulation(config)
        }
    }

    @Test
    fun `should throw if car leaves through an unknown gate`() {
        val car = Car(0, 0, 1, 500)
        val config = ParkingSimulationConfig(1, 5, listOf(car))
        assertThrows(ParkingSimulation.UnknownGateException::class.java) {
            ParkingSimulation(config)
        }
    }

    // run tests

    @Test
    fun `should run without exceptions`() {
        val (_, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(10, false)
        val config = ParkingSimulationConfig(10, 5, cars)
        val simulation = ParkingSimulation(config)
        simulation.run()
    }

    @Test
    fun `should run large without exceptions`() {
        val (_, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(1000, false)
        val config = ParkingSimulationConfig(1000, 500, cars)
        val simulation = ParkingSimulation(config)
        simulation.run()
    }

    // Timeline tests

    @Test
    fun `should correctly construct 1 frame from 1 car`() {
        val car = Car(0, 0)
        val arrival = CarEvent.getCarArrivalEvent(car)
        val expectedEvents = listOf(setOf(arrival))
        val config = ParkingSimulationConfig(1, 1, listOf(car))
        val timeline = TestableParkingSimulation(config)
        assertEquals(expectedEvents, timeline.timeframesEvents)
    }

    @Test
    fun `should correctly construct 2 frames from 1 car`() {
        val car = Car(0, 0, 1, 0)
        val arrival = CarEvent.getCarArrivalEvent(car)
        val departure = CarEvent.getCarDepartureEvent(car)
        val expectedFramesEvents = listOf(setOf(arrival), setOf(departure))
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
}
