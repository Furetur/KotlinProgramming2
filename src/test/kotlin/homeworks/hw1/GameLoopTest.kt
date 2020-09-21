package homeworks.hw1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GameLoopTest {

    private fun testOnNetwork(network: LocalNetwork) {
        val loop = GameLoop(network)
        loop.start()
        assert(network.isEveryComputerInfected)
    }

    @Test
    fun `should throw if there are no infected computers`() {
        val healthyNetwork = SegmentWindowsNetworkGenerator.makeNetwork(100, false, false)
        assertThrows(GameLoop.NoInfectedComputersException::class.java) {
            GameLoop(healthyNetwork)
        }
    }

    @Test
    fun `should run until all computers are infected in small network`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(10)
        testOnNetwork(network)
    }

    @Test
    fun `should run until all computers are infected in big segment network with one patient 0`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(100)
        testOnNetwork(network)
    }

    @Test
    fun `should run until all computers are infected in big segment network with multiple patients 0`() {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(100, true)
        testOnNetwork(network)
    }

    @Test
    fun `should run until all computers are infected in a circular network`() {
        val network = CircularWindowsNetworkGenerator.makeNetwork(100)
        testOnNetwork(network)
    }
}
