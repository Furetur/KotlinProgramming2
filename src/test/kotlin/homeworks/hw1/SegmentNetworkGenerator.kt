package homeworks.hw1

import homeworks.hw1.graph.Graph
import homeworks.hw1.random.IOutcomeRandomizer

object SegmentNetworkGenerator {
    fun makeMatrix(size: Int): MutableList<MutableList<Int>> {
        val matrix = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size - 1) {
            matrix[i][i + 1] = 1
            matrix[i + 1][i] = 1
        }
        return matrix
    }

    fun makeNetwork(computers: List<LocalNetwork.Computer>, randomizer: IOutcomeRandomizer): LocalNetwork {
        val graph = Graph(makeMatrix(computers.size))
        return LocalNetwork(computers, graph, randomizer)
    }

    fun makeNetwork(
            size: Int, randomizer: IOutcomeRandomizer,
            makeComputer: (Int) -> LocalNetwork.Computer
    ): LocalNetwork {
        val computers = (0 until size).map(makeComputer)
        return makeNetwork(computers, randomizer)
    }

    private fun makeDuoNetwork(
            computer1: LocalNetwork.Computer,
            computer2: LocalNetwork.Computer,
            randomizer: IOutcomeRandomizer
    ): LocalNetwork = makeNetwork(listOf(computer1, computer2), randomizer)

    fun makeDuoWindowsMacNetwork(randomizer: IOutcomeRandomizer): LocalNetwork =
            makeDuoNetwork(ComputersGenerator.makeWinPc(0, true), ComputersGenerator.makeMacOsPc(1), randomizer)

    fun makeDuoWindowsLinuxNetwork(randomizer: IOutcomeRandomizer): LocalNetwork =
            makeDuoNetwork(ComputersGenerator.makeWinPc(0, true), ComputersGenerator.makeLinuxPc(1), randomizer)

    fun makeLinuxNetwork(
            size: Int,
            endInfected: Boolean = false,
            randomizer: IOutcomeRandomizer
    ): LocalNetwork = makeNetwork(size, randomizer) {
        ComputersGenerator.makeLinuxPc(it, it == 0 || (endInfected && it == size - 1))
    }
}
