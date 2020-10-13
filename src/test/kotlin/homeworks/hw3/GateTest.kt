package homeworks.hw3

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GateTest {
    var api = TestServerApi()
    var gate = Gate(api)

    @BeforeEach
    fun init() {
        api = TestServerApi()
        gate = Gate(api)
    }

    @Test
    fun `acceptNewCar calls arrival api method`() {
        gate.acceptNewCar()
        assertEquals(1, api.carArrivalsCount)
    }
    @Test
    fun `each acceptNewCar calls arrival api method exactly 1 time`() {
        for (i in 0 until 100) {
            gate.acceptNewCar()
        }
        assertEquals(100, api.carArrivalsCount)
    }
    @Test
    fun `handleCarDeparture calls departure api method`() {
        gate.handleCarDeparture()
        assertEquals(1, api.carDeparturesCount)
    }
    @Test
    fun `each handleCarDeparture calls departure api method exactly 1 time`() {
        for (i in 0 until 100) {
            gate.handleCarDeparture()
        }
        assertEquals(100, api.carDeparturesCount)
    }
}
