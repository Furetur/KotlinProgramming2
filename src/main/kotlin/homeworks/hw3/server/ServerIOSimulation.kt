package homeworks.hw3.server

import homeworks.hw3.Timeline
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.util.Queue
import java.util.LinkedList

class ServerIOSimulation(private val serverEventsHandler: EventsHandler) {
    private val events: Queue<Timeline.CarEvent> = LinkedList()

    @ObsoleteCoroutinesApi
    private val ioThreadContext = newSingleThreadContext("ParkingSystemServer")

    @ObsoleteCoroutinesApi
    suspend fun handleEvent(event: Timeline.CarEvent) {
        events.add(event)
        withContext(ioThreadContext) {
            processEventFromQueue()
        }
    }

    private fun processEventFromQueue() {
        val event = events.poll() ?: return
        serverEventsHandler.handleEvent(event)
    }
}
