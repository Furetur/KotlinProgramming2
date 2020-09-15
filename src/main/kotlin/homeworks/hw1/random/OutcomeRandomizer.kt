package homeworks.hw1.random

import homeworks.hw1.LocalNetwork
import kotlin.random.Random

object OutcomeRandomizer : IOutcomeRandomizer {
    override fun getOutcome(): Int = Random.nextInt(1, LocalNetwork.MAX_PERCENTAGE + 1)
}
