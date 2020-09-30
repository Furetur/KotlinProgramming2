package homeworks.hw3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CarTest {

    private fun makeCar(departs: Boolean = false): Car = if (departs) {
        Car(0, 0, 1, 2)
    } else {
        Car(0, 0)
    }

    private val car = Car(0, 0, 1, 2)
    private val carThatNeverDeparts = Car(0, 0)

    @Test
    fun `should throw if arrival time is equal to departure time`() {
        assertThrows(Car.ArrivalTimeIsEqualToDepartureTimeException::class.java) {
            Car(0, 1, 0, 10)
        }
    }

    @Test
    fun `should make departure event time for car that departs once`() {
        assertEquals(1, car.departure?.time)
    }

    @Test
    fun `should make departure event gateId for car that departs once`() {
        assertEquals(2, car.departure?.gateId)
    }

    @Test
    fun `departure event should be null for car that never departs`() {
        assertNull(carThatNeverDeparts.departure)
    }
}
