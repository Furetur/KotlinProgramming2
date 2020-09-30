package homeworks.hw3.server

import homeworks.hw3.Timeline

class EventsCollector : EventsHandler {
    val collectedEvents = mutableSetOf<Timeline.CarEvent>()

    override fun handleEvent(event: Timeline.CarEvent) {
        collectedEvents.add(event)
    }
}
