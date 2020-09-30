package homeworks.hw3

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
        val simulation = Json.decodeFromString<ParkingSimulation>(file.readText())
        simulation.run()
    } catch (e: SerializationException) {
        println("File does not match the needed syntax")
    } catch (e: ParkingSimulation.UnknownGateException) {
        println(e.message)
    }
}
