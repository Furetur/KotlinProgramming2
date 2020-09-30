package homeworks.hw3

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.lang.IllegalArgumentException

@Serializable
data class Car(
    val arrivalTime: Int,
    val arrivalGateId: Int,
    val departureTime: Int? = null,
    val departureGateId: Int? = null
) {

    class ArrivalTimeIsEqualToDepartureTimeException : IllegalArgumentException(
        "Car arrival time cannot be equal to its departure time"
    )

    init {
        if (arrivalTime == departureTime) {
            throw ArrivalTimeIsEqualToDepartureTimeException()
        }
    }

    @Transient
    val arrival = Timeline.CarEvent(Timeline.CarEventType.ARRIVAL, this)

    @Transient
    val departure = if (departureTime != null) Timeline.CarEvent(Timeline.CarEventType.DEPARTURE, this) else null
}
