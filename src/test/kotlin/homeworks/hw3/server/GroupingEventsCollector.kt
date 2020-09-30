package homeworks.hw3.server

import homeworks.hw3.Timeline

class GroupingEventsCollector : EventsHandler {
    val collectedEventsGroups: MutableList<MutableSet<Timeline.CarEvent>> = mutableListOf()

    private var currentGroup = -1

    init {
        nextGroup()
    }

    fun nextGroup() {
        currentGroup += 1
        collectedEventsGroups.add(mutableSetOf())
    }

    override fun handleEvent(event: Timeline.CarEvent) {
        val group = collectedEventsGroups[currentGroup]
        group.add(event)
    }
}
