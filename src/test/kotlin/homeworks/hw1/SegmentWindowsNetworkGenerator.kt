package homeworks.hw1

import homeworks.hw1.graph.Graph

object SegmentWindowsNetworkGenerator {
    private val windows = OperatingSystem("win", "Windows", 100)

    private fun makeWinPc(infected: Boolean = false): LocalNetwork.Computer = LocalNetwork.Computer(windows, infected)

    fun makeMatrix(size: Int): List<List<Int>> {
        val matrix = MutableList(size) { MutableList(size) { 0 } }
        for (i in 0 until size - 1) {
            matrix[i][i + 1] = 1
            matrix[i + 1][i] = 1
        }
        return matrix
    }

    fun makeNetwork(
        size: Int,
        endInfected: Boolean = false,
        startInfected: Boolean = true,
    ): LocalNetwork {
        val matrix = makeMatrix(size)
        val graph = Graph(matrix)
        val computers = List(size) { makeWinPc((startInfected && it == 0) || (endInfected && it == size - 1)) }
        return LocalNetwork(computers, graph)
    }
}
