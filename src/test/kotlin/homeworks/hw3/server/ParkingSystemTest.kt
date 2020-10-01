package homeworks.hw3.server

import homeworks.hw3.simulation.ParkingSimulation

internal class ParkingSystemTest : ParkingEntityTest<ParkingSystem>(){
    private fun handleEvent(parkingSystem: ParkingSystem, event: ParkingSimulation.CarEvent) {
        when(event.type) {
            ParkingSimulation.CarEventType.ARRIVAL -> parkingSystem.acceptCar(event.car)
            ParkingSimulation.CarEventType.DEPARTURE -> parkingSystem.handleCarDeparture(event.car)
        }
    }

    override fun handleEvents(parkingSystem: ParkingSystem, events: List<ParkingSimulation.CarEvent>) {
        for (event in events) {
            handleEvent(parkingSystem, event)
        }
    }

    override fun getEmptySpacesCount(parkingSystem: ParkingSystem): Int = parkingSystem.emptySpacesCount

    override fun getOccupiedSpacesCount(parkingSystem: ParkingSystem): Int = parkingSystem.occupiedSpacesCount

    override fun createEntity(parkingSpacesCount: Int) = ParkingSystem(parkingSpacesCount)
}
