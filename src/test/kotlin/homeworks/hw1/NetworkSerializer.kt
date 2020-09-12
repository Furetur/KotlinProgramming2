package homeworks.hw1

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import homeworks.hw1.InitialStateParser
import homeworks.hw1.LocalNetwork

object NetworkSerializer {
    fun serializeNetwork(network: LocalNetwork): String {
        val state = InitialStateParser.InitialState(
                convertOperatingSystems(network),
                convertComputers(network),
                network.graph.matrix.toList()
        )
        return Json.encodeToString<InitialStateParser.InitialState>(state)
    }

    private fun convertComputers(network: LocalNetwork): List<InitialStateParser.ComputerConfig> {
        return network.computers.map { InitialStateParser.ComputerConfig(it.os.id, it.isInitiallyInfected) }
    }

    private fun convertOperatingSystems(network: LocalNetwork): Map<String, InitialStateParser.OperatingSystemConfig> {
        val allOperatingSystems = network.computers.map { it.os }.distinctBy { it.id }
        return allOperatingSystems.associateBy(
                { it.id },
                { InitialStateParser.OperatingSystemConfig(it.name, it.infectionPercentageChance) }
        )
    }
}