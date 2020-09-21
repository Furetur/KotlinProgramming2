package homeworks.hw1

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import homeworks.hw1.graph.Graph
import java.io.File
import java.lang.IllegalArgumentException

object InitialStateParser {

    fun getLocalNetworkFromFile(file: File): LocalNetwork = getLocalNetworkFromString(file.readText())

    fun getLocalNetworkFromString(serializedState: String): LocalNetwork {
        val state = Json.decodeFromString<InitialState>(serializedState)
        val computers = parseComputers(state)
        val graph = parseGraph(state)
        return LocalNetwork(computers, graph)
    }

    private fun parseOperatingSystems(state: InitialState): Map<String, OperatingSystem> =
            state.os.mapValues { OperatingSystem(it.key, it.value.name, it.value.infectionPercentageChance) }

    private fun parseComputers(state: InitialState): List<LocalNetwork.Computer> {
        val operatingSystems = parseOperatingSystems(state)
        return state.computers.mapIndexed { id, config ->
            LocalNetwork.Computer(
                    id,
                    operatingSystems[config.os] ?: throw OperatingSystemIsNotDefinedException(config.os),
                    config.infected ?: false
            )
        }
    }

    private fun parseGraph(state: InitialState): Graph = Graph(state.matrix)

    @Serializable
    data class InitialState(
        val os: Map<String, OperatingSystemConfig>,
        val computers: List<ComputerConfig>,
        val matrix: List<List<Int>>
    )

    @Serializable
    data class OperatingSystemConfig(val name: String, val infectionPercentageChance: Int)

    @Serializable
    data class ComputerConfig(val os: String, val infected: Boolean? = false)

    class OperatingSystemIsNotDefinedException(osId: String) :
            IllegalArgumentException("Operating system with id=$osId (referenced by computer) does not exist")
}
