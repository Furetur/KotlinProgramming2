package homeworks.hw3

import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException

@Serializable
class Car(
    val arrivalTime: Int,
    val arrivalGateId: Int,
    val departureTime: Int? = null,
    val departureGateId: Int? = null
) {
    class ArrivalTimeIsEqualToDepartureTimeException : IllegalArgumentException(
        "Car arrival time cannot be equal to its departure time"
    )

    init {
        if (departureTime != null && arrivalTime >= departureTime) {
            throw ArrivalTimeIsEqualToDepartureTimeException()
        }
    }
}
