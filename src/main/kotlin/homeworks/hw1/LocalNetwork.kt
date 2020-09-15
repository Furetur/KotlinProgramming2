package homeworks.hw1

import homeworks.hw1.graph.Graph
import homeworks.hw1.random.IOutcomeRandomizer
import homeworks.hw1.random.OutcomeRandomizer
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class LocalNetwork(
    val computers: List<Computer>,
    val graph: Graph,
    private val outcomeRandomizer: IOutcomeRandomizer = OutcomeRandomizer
) {

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
        val outcome = outcomeRandomizer.getOutcome()
        if (outcome < 1 || outcome > MAX_PERCENTAGE) {
            throw IllegalOutcomeException()
        }

        val infectableNodes = getInfectableNodes()

        for (node in infectableNodes) {
            tryInfectNode(node, outcome)
        }
    }

    private fun tryInfectNode(node: NetworkNode, outcome: Int): Boolean {
        if (node.isInfected) {
            throw AlreadyInfectedError()
        }

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

    class IllegalOutcomeException : IllegalArgumentException()

    class AlreadyInfectedError : IllegalStateException()

    data class Computer(val id: Int, val os: OperatingSystem, val isInitiallyInfected: Boolean)

    class NumberOfComputersIsNotEqualToNumberOfVertices : IllegalArgumentException()
}
