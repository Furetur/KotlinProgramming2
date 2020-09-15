package homeworks.hw1

import homeworks.hw1.graph.Graph

object CircularWindowsNetworkGenerator {
    fun makeMatrix(size: Int): MutableList<MutableList<Int>> {
        val matrix = SegmentNetworkGenerator.makeMatrix(size)
        val lastVertexId = size - 1
        matrix[0][lastVertexId] = 1
        matrix[lastVertexId][0] = 1
        return matrix
    }

    fun makeNetwork(size: Int): LocalNetwork {
        val graph = Graph(makeMatrix(size))
        val computers = List(size) { ComputersGenerator.makeWinPc(it, it == 0) }
        return LocalNetwork(computers, graph)
    }
}
