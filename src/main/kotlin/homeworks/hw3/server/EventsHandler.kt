package homeworks.hw3.server

import homeworks.hw3.Timeline

interface EventsHandler {
    fun handleEvent(event: Timeline.CarEvent)
}
