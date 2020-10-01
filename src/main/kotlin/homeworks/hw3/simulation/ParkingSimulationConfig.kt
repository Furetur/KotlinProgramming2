package homeworks.hw3.simulation

import homeworks.hw3.Car
import kotlinx.serialization.Serializable

@Serializable
data class ParkingSimulationConfig(val gatesCount: Int, val parkingSpacesCount: Int, val cars: List<Car>)
