package homeworks.hw1.random

class MockConstantOutcomeRandomizer(private val value: Int) : IOutcomeRandomizer {
    override fun getOutcome(): Int = value
}
