package homeworks.hw1.random

class MockRangeOutcomeRandomizer(private val start: Int, endInclusive: Int) : IOutcomeRandomizer {
    private val intervalLength = endInclusive - start + 1
    private var currentValue = start

    override fun getOutcome(): Int {
        val prevValue = currentValue
        val nextValue = (currentValue - start + 1) % intervalLength + start
        currentValue = nextValue
        return prevValue
    }
}
