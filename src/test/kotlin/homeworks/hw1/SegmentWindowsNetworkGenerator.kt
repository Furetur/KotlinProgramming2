package homeworks.hw1

import homeworks.hw1.random.IOutcomeRandomizer
import homeworks.hw1.random.OutcomeRandomizer

object SegmentWindowsNetworkGenerator {
    fun makeNetwork(
            size: Int,
            endInfected: Boolean = false,
            startInfected: Boolean = true,
            randomizer: IOutcomeRandomizer = OutcomeRandomizer,
    ): LocalNetwork = SegmentNetworkGenerator.makeNetwork(size, randomizer) {
        ComputersGenerator.makeWinPc(it, (startInfected && it == 0) || (endInfected && it == size - 1))
    }

    fun makeNetworkWithMacInMiddle(size: Int, randomizer: IOutcomeRandomizer): LocalNetwork {
        return makeNetworkWithSpecialComputerInMiddle(size, ComputersGenerator.makeMacOsPc(size / 2), randomizer)
    }

    fun makeNetworkWithLinuxInMiddle(size: Int, randomizer: IOutcomeRandomizer): LocalNetwork {
        return makeNetworkWithSpecialComputerInMiddle(size, ComputersGenerator.makeLinuxPc(size / 2), randomizer)
    }

    private fun makeNetworkWithSpecialComputerInMiddle(
            size: Int,
            specialComputer: LocalNetwork.Computer,
            randomizer: IOutcomeRandomizer = OutcomeRandomizer
    ): LocalNetwork {
        val middle = size / 2
        return SegmentNetworkGenerator.makeNetwork(size, randomizer) {
            if (it != middle)
                ComputersGenerator.makeWinPc(it, it == 0)
            else
                specialComputer
        }
    }
}
