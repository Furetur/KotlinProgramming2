package homeworks.hw3

import homeworks.hw3.server.ParkingSystem
import homeworks.hw3.simulation.ParkingSimulation
import homeworks.hw3.simulation.ParkingSimulationConfig
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

const val FILE_PATH = "./src/main/resources/homeworks/hw3/parking.json"

@ObsoleteCoroutinesApi
fun main() {
    val file = File(FILE_PATH)
    if (!file.exists()) {
        println("File was not found at path $FILE_PATH")
        return
    }
    try {
        val simulationConfig = Json.decodeFromString<ParkingSimulationConfig>(file.readText())
        val simulation = ParkingSimulation(simulationConfig)
        simulation.run()
    } catch (e: SerializationException) {
        println(e.message)
    } catch (e: ParkingSimulation.UnknownGateException) {
        println(e.message)
    } catch (e: ParkingSystem.NegativeParkingSpacesException) {
        println(e.message)
    } catch (e: ParkingSimulation.NotPositiveGatesCountException) {
        println(e.message)
    } catch (e: Car.ArrivalTimeIsEqualToDepartureTimeException) {
        println(e.message)
    }
}
