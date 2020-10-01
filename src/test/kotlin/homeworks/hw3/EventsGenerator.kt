package homeworks.hw3

import homeworks.hw3.simulation.ParkingSimulation

object EventsGenerator {
    fun makeArrivalEvent(car: Car): ParkingSimulation.CarEvent =
        ParkingSimulation.CarEvent(ParkingSimulation.CarEventType.ARRIVAL, car)

    fun makeDepartureEvent(car: Car): ParkingSimulation.CarEvent? =
        if (car.departureTime != null) ParkingSimulation.CarEvent(
            ParkingSimulation.CarEventType.DEPARTURE,
            car
        ) else null
}
