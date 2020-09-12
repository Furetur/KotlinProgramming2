package homeworks.hw1

import homeworks.hw1.NetworkSerializer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class InitialStateParserTest {

    private fun testOnWindowsSegmentNetwork(size: Int, endInfected: Boolean = false) {
        val network = SegmentWindowsNetworkGenerator.makeNetwork(size, endInfected)
        val serializedNetwork = NetworkSerializer.serializeNetwork(network)
        val parsedNetwork = InitialStateParser.getLocalNetworkFromString(serializedNetwork)
        assertEquals(network, parsedNetwork)
    }

    @Test
    fun `should throw if computer references undefined os`() {
        val file = File("./src/test/resources/homeworks/hw1/undefinedOs.json")
        assertThrows(InitialStateParser.OperatingSystemIsNotDefinedException::class.java) {
            InitialStateParser.getLocalNetworkFromFile(file)
        }
    }

    @Test
    fun `should throw if number of computers is greater than number of vertices`() {
        val file = File("./src/test/resources/homeworks/hw1/moreComputersThanVertices.json")
        assertThrows(LocalNetwork.NumberOfComputersIsNotEqualToNumberOfVertices::class.java) {
            InitialStateParser.getLocalNetworkFromFile(file)
        }
    }

    @Test
    fun `should throw if number of computers is smaller than number of vertices`() {
        val file = File("./src/test/resources/homeworks/hw1/lessComputersThanVertices.json")
        assertThrows(LocalNetwork.NumberOfComputersIsNotEqualToNumberOfVertices::class.java) {
            InitialStateParser.getLocalNetworkFromFile(file)
        }
    }

    @Test
    fun `should parse small windows segment network correctly`() {
        testOnWindowsSegmentNetwork(4, false)
    }

    @Test
    fun `should parse small windows segment network correctly with infected end`() {
        testOnWindowsSegmentNetwork(4, true)
    }

    @Test
    fun `should parse huge windows segment network`() {
        testOnWindowsSegmentNetwork(1000, true)
    }
}
