package homeworks.hw1

import homeworks.hw1.LocalNetwork
import homeworks.hw1.OperatingSystem
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import homeworks.hw1.graph.Graph

internal class LocalNetworkTest {

    private val windows = OperatingSystem("win", "Windows", 100)
    private val linux = OperatingSystem("linux", "Arch Linux (nerds only)", 50)
    private val mac = OperatingSystem("mac", "MacOS", 0)

    private fun makeWinPc(infected: Boolean = false): LocalNetwork.Computer = LocalNetwork.Computer(windows, infected)
    private fun makeLinuxPc(infected: Boolean = false): LocalNetwork.Computer = LocalNetwork.Computer(linux, infected)
    private fun makeMacOsPc(infected: Boolean = false): LocalNetwork.Computer = LocalNetwork.Computer(mac, infected)

    private val simpleGraph = Graph(
            listOf(
                    listOf(0, 1, 0),
                    listOf(1, 0, 1),
                    listOf(0, 1, 0),
            )
    )

    private fun makeSimpleNetwork(isWinInfected: Boolean, isMacInfected: Boolean, isLinuxInfected: Boolean): LocalNetwork {
        val computers = listOf(makeWinPc(isWinInfected), makeMacOsPc(isMacInfected), makeLinuxPc(isLinuxInfected))
        return LocalNetwork(computers, simpleGraph)
    }

    @Test
    fun `should throw if number of computers is greater than number of vertices`() {
        val computers = List(100) { makeWinPc() }
        val matrix = SegmentWindowsNetworkGenerator.makeMatrix(5)
        assertThrows(LocalNetwork.NumberOfComputersIsNotEqualToNumberOfVertices::class.java) {
            LocalNetwork(computers, Graph(matrix))
        }
    }

    @Test
    fun `should throw if number of computers is smaller than number of vertices`() {
        val computers = List(10) { makeWinPc() }
        val matrix = SegmentWindowsNetworkGenerator.makeMatrix(50)
        assertThrows(LocalNetwork.NumberOfComputersIsNotEqualToNumberOfVertices::class.java) {
            LocalNetwork(computers, Graph(matrix))
        }
    }

    @Test
    fun `2 windows segment networks should be equal if input data is the same`() {
        val network1 = SegmentWindowsNetworkGenerator.makeNetwork(100)
        val network2 = SegmentWindowsNetworkGenerator.makeNetwork(100)
        assertEquals(network1, network2)
    }

    @Test
    fun `2 windows segment networks should be not equal if one has 1 infected pc and another has 2`() {
        val network1 = SegmentWindowsNetworkGenerator.makeNetwork(100)
        val network2 = SegmentWindowsNetworkGenerator.makeNetwork(100, true)
        assertNotEquals(network1, network2)
    }

    @Test
    fun `windows segment networks should not be equal if sizes are different`() {
        val network1 = SegmentWindowsNetworkGenerator.makeNetwork(100)
        val network2 = SegmentWindowsNetworkGenerator.makeNetwork(10)
        assertNotEquals(network1, network2)
    }

    @Test
    fun `windows will always be infected by neighbour pcs in simple network`() {
        val simpleNetwork = makeSimpleNetwork(false, true, false)
        val winPc = simpleNetwork.computers[0]
        simpleNetwork.trySpreadVirus()
        assert(simpleNetwork.isComputerInfected(winPc))
    }

    @Test
    fun `mac is never infected in simple network`() {
        val simpleNetwork = makeSimpleNetwork(true, false, false)
        val macPc = simpleNetwork.computers[1]
        simpleNetwork.trySpreadVirus()
        assertFalse(simpleNetwork.isComputerInfected(macPc))
    }

    @Test
    fun `computers are infected step by step in segment network 1`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(100)
        val computers = network.computers
        for (i in 0 until 50) {
            network.trySpreadVirus()
        }
        assertEquals(computers.subList(0, 51).toSet(), network.infectedComputers.toSet())
    }

    @Test
    fun `computers are infected step by step in segment network 2`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(1000)
        val computers = network.computers
        for (i in 0 until 100) {
            network.trySpreadVirus()
        }
        assertEquals(computers.subList(0, 101).toSet(), network.infectedComputers.toSet())
    }

    @Test
    fun `each computer is infected in segment network of size 100 after 99 steps`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(100)
        for (i in 0 until 99) {
            network.trySpreadVirus()
        }
        assert(network.isEveryComputerInfected)
    }

    @Test
    fun `computers are infected step by step in segment network with both ends infected`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(1000, true)
        val computers = network.computers
        val steps = 1

        for (i in 0 until steps) {
            network.trySpreadVirus()
        }

        val expected = (computers.subList(0, steps + 1) + computers.subList(1000 - steps - 1, 1000)).toSet()
        assertEquals(expected, network.infectedComputers.toSet())
    }
}
