package homeworks.hw1

import homeworks.hw1.graph.Graph
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.random.Random

class LocalNetwork(val computers: List<Computer>, val graph: Graph) {

    init {
        if (computers.size != graph.vertices.size) {
            throw NumberOfComputersIsNotEqualToNumberOfVertices()
        }
    }

    private val nodes = computers.mapIndexed { index, computer -> NetworkNode(computer, graph.vertices[index]) }

    private val infectedNodes = nodes.filter { it.isInfected }.toMutableList()

    val infectedComputers: List<Computer>
        get() = infectedNodes.map { it.computer }

    val isEveryComputerInfected: Boolean
        get() = infectedNodes.size == computers.size
    private val networkMap = nodes.associateBy { it.vertex }

    private val computersMap = nodes.associateBy({ it.computer }, { it })

    companion object {
        const val MAX_PERCENTAGE = 100
        const val DEFAULT_PRIME = 31
    }

    fun isComputerInfected(computer: Computer): Boolean = computersMap[computer]?.isInfected ?: false

    fun trySpreadVirus() {
        val infectableNodes = getInfectableNodes()

        for (node in infectableNodes) {
            tryInfectNode(node)
        }
    }

    private fun tryInfectNode(node: NetworkNode): Boolean {
        if (node.isInfected) {
            throw AlreadyInfectedError()
        }

        val outcome = Random.nextInt(
                1,
                Companion.MAX_PERCENTAGE
        )
        return if (outcome <= node.computer.os.infectionPercentageChance) {
            node.isInfected = true
            infectedNodes.add(node)
            true
        } else {
            false
        }
    }

    private fun getInfectableNodes(): Set<NetworkNode> {
        val infectableNodes = mutableSetOf<NetworkNode>()

        for (node in infectedNodes) {
            val currentInfectableNodes =
                    node.vertex.getNeighbours().mapNotNull { networkMap[it] }.filter { !it.isInfected }
            infectableNodes.addAll(currentInfectableNodes)
        }
        return infectableNodes
    }

    override fun equals(other: Any?): Boolean {
        return other is LocalNetwork && other.graph == graph && other.computers == computers
    }

    override fun hashCode(): Int {
        var result = nodes.hashCode()
        result = DEFAULT_PRIME * result + graph.hashCode()
        result = DEFAULT_PRIME * result + infectedNodes.hashCode()
        result = DEFAULT_PRIME * result + networkMap.hashCode()
        return result
    }

    data class NetworkNode(
        val computer: Computer,
        val vertex: Graph.Vertex,
        var isInfected: Boolean = computer.isInitiallyInfected
    )

    class AlreadyInfectedError : IllegalStateException()

    data class Computer(val os: OperatingSystem, val isInitiallyInfected: Boolean)

    class NumberOfComputersIsNotEqualToNumberOfVertices : IllegalArgumentException()
}
