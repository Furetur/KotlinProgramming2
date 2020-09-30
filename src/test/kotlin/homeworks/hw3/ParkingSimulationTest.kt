package homeworks.hw3

import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ParkingSimulationTest {
    @ObsoleteCoroutinesApi
    @Test
    fun `should throw if car enters through an unknown gate`() {
        val car = Car(0, 500, 1, 500)
        assertThrows(ParkingSimulation.UnknownGateException::class.java) {
            ParkingSimulation(1, 5, listOf(car))
        }
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should throw if car leaves through an unknown gate`() {
        val car = Car(0, 0, 1, 500)
        assertThrows(ParkingSimulation.UnknownGateException::class.java) {
            ParkingSimulation(1, 5, listOf(car))
        }
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should run without exceptions`() {
        val (_, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(10, false)
        val simulation = ParkingSimulation(10, 5, cars)
        simulation.run()
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should run large without exceptions`() {
        val (_, cars) = TimeframesWithCarsGenerator.generateSimpleTimeframesWithCars(1000, false)
        val simulation = ParkingSimulation(1000, 500, cars)
        simulation.run()
    }
}
