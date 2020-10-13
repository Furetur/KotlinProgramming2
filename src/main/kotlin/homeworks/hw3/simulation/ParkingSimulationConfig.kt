package homeworks.hw3.simulation

import kotlinx.serialization.Serializable

@Serializable
data class ParkingSimulationConfig(val gatesCount: Int, val parkingSpacesCount: Int, val cars: List<Car>)
