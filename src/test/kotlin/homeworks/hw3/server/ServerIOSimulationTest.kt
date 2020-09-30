package homeworks.hw3.server

import homeworks.hw3.TimelessEventsGenerator
import homeworks.hw3.Timeline
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ServerIOSimulationTest {

    @ObsoleteCoroutinesApi
    private fun testIfHandlesAllEvents(size: Int) = runBlocking {
        val collector = EventsCollector()
        val io = ServerIOSimulation(collector)

        val events = mutableSetOf<Timeline.CarEvent>()

        for (i in 0 until size) {
            val event = TimelessEventsGenerator.makeArrivalEvent()
            io.handleEvent(event)
            events.add(event)
        }

        assertEquals(events, collector.collectedEvents)
    }

    @ObsoleteCoroutinesApi
    private fun testIfHandlesGroupedEvents(groupsCount: Int, groupSize: Int) = runBlocking {
        val collector = GroupingEventsCollector()
        val io = ServerIOSimulation(collector)

        val events = mutableListOf(mutableSetOf<Timeline.CarEvent>())

        for (groupIndex in 0 until groupsCount) {
            val currentGroup = events[groupIndex]
            for (eventIndex in 0 until groupSize) {
                val event = TimelessEventsGenerator.makeArrivalEvent()
                io.handleEvent(event)
                currentGroup.add(event)
            }
            collector.nextGroup()
            events.add(mutableSetOf())
        }

        assertEquals(events, collector.collectedEventsGroups)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should handle all 10 events`() {
        testIfHandlesAllEvents(10)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should handle all 100 events`() {
        testIfHandlesAllEvents(100)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should handle all 1000 events`() {
        testIfHandlesAllEvents(1000)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should handle 10 groups of 10 events`() {
        testIfHandlesGroupedEvents(10, 10)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should handle 100 groups of 100 events`() {
        testIfHandlesGroupedEvents(100, 100)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `should handle 1000 groups of 100 events`() {
        testIfHandlesGroupedEvents(1000, 100)
    }
}
