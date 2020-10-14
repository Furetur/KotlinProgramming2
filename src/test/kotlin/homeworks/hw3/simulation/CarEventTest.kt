package homeworks.hw3.simulation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CarEventTest {
    @Test
    fun `should correctly get arrival event from car`() {
        val car = Car(0, 1, 2, 3)
        val expectedEvent = CarEvent(CarEventType.ARRIVAL, 1, 0)
        val actualEvent = CarEvent.getCarArrivalEvent(car)
        assertEquals(expectedEvent, actualEvent)
    }

    @Test
    fun `should correctly get departure event from car`() {
        val car = Car(0, 1, 2, 3)
        val expectedEvent = CarEvent(CarEventType.DEPARTURE, 3, 2)
        val actualEvent = CarEvent.getCarDepartureEvent(car)
        assertEquals(expectedEvent, actualEvent)
    }

    @Test
    fun `getCarDepartureEvent should return null if car never departs`() {
        val car = Car(0, 1)
        val actualEvent = CarEvent.getCarDepartureEvent(car)
        assertEquals(null, actualEvent)
    }
}
