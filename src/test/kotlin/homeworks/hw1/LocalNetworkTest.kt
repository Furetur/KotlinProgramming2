package homeworks.hw1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import homeworks.hw1.graph.Graph
import homeworks.hw1.random.MockConstantOutcomeRandomizer
import homeworks.hw1.random.MockRangeOutcomeRandomizer
import homeworks.hw1.random.OutcomeRandomizer

internal class LocalNetworkTest {
    private fun makeWinPc(infected: Boolean = false): LocalNetwork.Computer
            = ComputersGenerator.makeWinPc(0, infected)
    private fun makeLinuxPc(infected: Boolean = false): LocalNetwork.Computer
            = ComputersGenerator.makeLinuxPc(1, infected)
    private fun makeMacOsPc(infected: Boolean = false): LocalNetwork.Computer
            = ComputersGenerator.makeMacOsPc(2, infected)

    private fun makeSimpleNetwork(
            isWinInfected: Boolean,
            isMacInfected: Boolean,
            isLinuxInfected: Boolean
    ): LocalNetwork {
        val computers = listOf(makeWinPc(isWinInfected), makeMacOsPc(isMacInfected), makeLinuxPc(isLinuxInfected))
        return SegmentNetworkGenerator.makeNetwork(computers, OutcomeRandomizer)
    }

    @Test
    fun `should throw if number of computers is greater than number of vertices`() {
        val computers = List(100) { makeWinPc() }
        val matrix = SegmentNetworkGenerator.makeMatrix(5)
        assertThrows(LocalNetwork.NumberOfComputersIsNotEqualToNumberOfVertices::class.java) {
            LocalNetwork(computers, Graph(matrix))
        }
    }

    @Test
    fun `should throw if number of computers is smaller than number of vertices`() {
        val computers = List(10) { makeWinPc() }
        val matrix = SegmentNetworkGenerator.makeMatrix(50)
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

    @Test
    fun `computers should be infected step by step in a small circular network`() {
        val network = CircularWindowsNetworkGenerator.makeNetwork(10)
        val computers = network.computers

        val steps = 2

        for (i in 0 until steps) {
            network.trySpreadVirus()
        }

        val expected = computers.subList(0, steps + 1) + computers.subList(computers.size - steps, computers.size)
        assertEquals(expected.toSet(), network.infectedComputers.toSet())
    }

    @Test
    fun `computers should be infected step by step in a big circular network`() {
        val network = CircularWindowsNetworkGenerator.makeNetwork(1000)
        val computers = network.computers

        val steps = 500

        for (i in 0 until steps) {
            network.trySpreadVirus()
        }

        val expected = computers.subList(0, steps + 1) + computers.subList(computers.size - steps, computers.size)
        assertEquals(expected.toSet(), network.infectedComputers.toSet())
    }

    @Test
    fun `trySpreadVirus throws if outcome is greater than 100`() {
        val randomizer = MockConstantOutcomeRandomizer(101)
        val network = SegmentWindowsNetworkGenerator.makeNetwork(
                100,
                endInfected = false,
                startInfected = true,
                randomizer = randomizer
        )
        assertThrows(LocalNetwork.IllegalOutcomeException::class.java) {
            network.trySpreadVirus()
        }
    }

    @Test
    fun `trySpreadVirus throws if outcome is less than 1`() {
        val randomizer = MockConstantOutcomeRandomizer(0)
        val network = SegmentWindowsNetworkGenerator.makeNetwork(
                100,
                endInfected = false,
                startInfected = true,
                randomizer = randomizer
        )
        assertThrows(LocalNetwork.IllegalOutcomeException::class.java) {
            network.trySpreadVirus()
        }
    }

    @Test
    fun `mac is not infectable in duo network with any outcome`() {
        val randomizer = MockRangeOutcomeRandomizer(1, 100)
        val network = SegmentNetworkGenerator.makeDuoWindowsMacNetwork(randomizer)
        for (i in 1..100) {
            network.trySpreadVirus()
        }
        assertEquals(network.computers.subList(0, 1), network.infectedComputers)
    }

    @Test
    fun `linux is not infectable in duo network with outcomes larger than 50`() {
        val randomizer = MockRangeOutcomeRandomizer(51, 100)
        val network = SegmentNetworkGenerator.makeDuoWindowsLinuxNetwork(randomizer)
        for (i in 1..100) {
            network.trySpreadVirus()
        }
        assertEquals(network.computers.subList(0, 1), network.infectedComputers)
    }

    @Test
    fun `linux is infectable in duo network with outcome 1`() {
        val randomizer = MockConstantOutcomeRandomizer(1)
        val network = SegmentNetworkGenerator.makeDuoWindowsLinuxNetwork(randomizer)
        network.trySpreadVirus()
        assert(network.isEveryComputerInfected)
    }

    @Test
    fun `mac will never be infected in the middle of segment by any outcome`() {
        val randomizer = MockRangeOutcomeRandomizer(1, 100)
        val network = SegmentWindowsNetworkGenerator.makeNetworkWithMacInMiddle(5, randomizer)

        for (i in 1..4) {
            for (outcome in 1..100) {
                network.trySpreadVirus()
            }
        }
        assertEquals(network.computers.subList(0, 2).toSet(), network.infectedComputers.toSet())
    }

    @Test
    fun `linux will be infected in the middle of segment by outcome 50`() {
        val randomizer = MockConstantOutcomeRandomizer(50)
        val network = SegmentWindowsNetworkGenerator.makeNetworkWithLinuxInMiddle(5, randomizer)
        for (i in 1..4) {
            network.trySpreadVirus()
        }
        assert(network.isEveryComputerInfected)
    }

    @Test
    fun `linux will be not infected in segment network by any outcome greater than 50`() {
        val randomizer = MockRangeOutcomeRandomizer(51, 100)
        val network = SegmentWindowsNetworkGenerator.makeNetworkWithLinuxInMiddle(5, randomizer)

        for (i in 1..4) {
            for (outcome in 51..100) {
                network.trySpreadVirus()
            }
        }
        assertEquals(network.computers.subList(0, 2).toSet(), network.infectedComputers.toSet())
    }

    @Test
    fun `big segment linux networks are not infectable if outcome is greater than 50`() {
        val randomizer = MockRangeOutcomeRandomizer(51, 100)
        val network = SegmentNetworkGenerator.makeLinuxNetwork(101, false, randomizer)
        for (i in 1..99) {
            for (outcome in 51..100) {
                network.trySpreadVirus()
            }
        }
        val expected = setOf(network.computers[0])
        assertEquals(expected, network.infectedComputers.toSet())
    }

    @Test
    fun `big segment linux networks with infected end are not infectable if outcome is greater than 51`() {
        val randomizer = MockRangeOutcomeRandomizer(51, 100)
        val network = SegmentNetworkGenerator.makeLinuxNetwork(101, true, randomizer)
        for (i in 1..100) {
            for (outcome in 51..100) {
                network.trySpreadVirus()
            }
        }
        val expected = setOf(network.computers[0], network.computers.last())
        assertEquals(expected, network.infectedComputers.toSet())
    }

    @Test
    fun `big segment linux networks are easy to infect if outcome is less or equal than 50`() {
        val randomizer = MockRangeOutcomeRandomizer(1, 50)
        val network = SegmentNetworkGenerator.makeLinuxNetwork(100, false, randomizer)
        for (i in 1..99) {
            network.trySpreadVirus()
        }
        assert(network.isEveryComputerInfected)
    }

    @Test
    fun `big segment networks with mac in middle are not infectable from ends`() {
        val randomizer = MockRangeOutcomeRandomizer(1, 100)
        val network = SegmentWindowsNetworkGenerator.makeNetworkWithMacInMiddle(100, randomizer)
        for (i in 1..99) {
            for (outcome in 1..100) {
                network.trySpreadVirus()
            }
        }
        assertFalse(network.isEveryComputerInfected)
    }

    @Test
    fun `big networks with linux in middle are not infected with outcomes greater than 50`() {
        val randomizer = MockRangeOutcomeRandomizer(51, 100)
        val network = SegmentWindowsNetworkGenerator.makeNetworkWithLinuxInMiddle(100, randomizer)
        for (i in 1..99) {
            for (outcome in 51..100) {
                network.trySpreadVirus()
            }
        }
        assertFalse(network.isEveryComputerInfected)
    }

    @Test
    fun `big networks with linux in middle are infected by outcomes less or equal than 50`() {
        val randomizer = MockRangeOutcomeRandomizer(1, 50)
        val network = SegmentWindowsNetworkGenerator.makeNetworkWithLinuxInMiddle(100, randomizer)
        for (i in 1..99) {
            network.trySpreadVirus()
        }
        assert(network.isEveryComputerInfected)
    }
}
