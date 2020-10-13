package homeworks.hw3

import homeworks.hw3.simulation.Car
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CarTest {
    @Test
    fun `should throw if arrival time is equal to departure time`() {
        assertThrows(Car.ArrivalTimeIsEqualToDepartureTimeException::class.java) {
            Car(0, 1, 0, 10)
        }
    }

    @Test
    fun `should throw if arrival time is greater than departure time`() {
        assertThrows(Car.ArrivalTimeIsEqualToDepartureTimeException::class.java) {
            Car(1, 1, 0, 10)
        }
    }

    @Test
    fun `should not throw if arrival time is less than departure time`() {
        Car(0, 1, 1, 10)
    }
}
