package homeworks.hw3.simulation

data class CarEvent(val type: CarEventType, val gateId: Int, val time: Int) {
    companion object {
        fun getCarArrivalEvent(car: Car) = CarEvent(CarEventType.ARRIVAL, car.arrivalGateId, car.arrivalTime)

        fun getCarDepartureEvent(car: Car): CarEvent? {
            val gateId = car.departureGateId
            val time = car.departureTime
            return if (gateId != null && time != null) {
                CarEvent(CarEventType.DEPARTURE, gateId, time)
            } else {
                null
            }
        }
    }
}
