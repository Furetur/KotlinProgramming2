package homeworks.hw3

import homeworks.hw3.server.ParkingSystem
import homeworks.hw3.server.ServerApiSimulation
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.lang.IllegalArgumentException

@Serializable
class ParkingSimulation(private val gatesCount: Int, private val parkingSpacesCount: Int, private val cars: List<Car>) {
    @Transient
    private val serverApi = ServerApiSimulation(parkingSpacesCount)
    @Transient
    private val gates = List(gatesCount) { Gate(it, serverApi) }

    @Transient
    private val timeline = Timeline(cars)

    init {
        validateInput()
    }

    private fun validateInput() {
        for (car in cars) {
            if (!gates.indices.contains(car.arrival.gateId)) {
                throw UnknownGateException(car.arrival)
            }
            val departure = car.departure
            if (departure != null && !gates.indices.contains(departure.gateId)) {
                throw UnknownGateException(departure)
            }
        }
    }

    @ObsoleteCoroutinesApi
    fun run() {
        for (frame in timeline.frames) {
            logTimeframeStart(frame)
            runTimeframe(frame)
            logTimeframeEnd()
        }
    }

    @ObsoleteCoroutinesApi
    private fun runTimeframe(timeframe: Timeline.Timeframe) = runBlocking {
        for (event in timeframe.events) {
            launch {
                try {
                    dispatchEvent(event)
                    logEvent(event)
                } catch (e: ParkingSystem.NoFreeParkingSpacesException) {
                    println(
                        "\tCar tried to use the parking but there were no available parking spaces. The car was ignored"
                    )
                }
            }
        }
    }

    @ObsoleteCoroutinesApi
    private suspend fun dispatchEvent(event: Timeline.CarEvent) {
        if (!gates.indices.contains(event.gateId)) {
            throw UnknownGateException(event)
        }
        val gate = gates[event.gateId]
        gate.sendEvent(event)
    }

    private fun logTimeframeStart(frame: Timeline.Timeframe) {
        println("START Timeframe time=${frame.time}")
    }

    private fun logTimeframeEnd() {
        println("END")
        println("> There are ${serverApi.emptySpacesCount}/$parkingSpacesCount available parking spaces")
    }

    private fun logEvent(event: Timeline.CarEvent) {
        println(
            when (event.type) {
                Timeline.CarEventType.ARRIVAL ->
                    "\tCar entered the parking through the gate id=${event.gateId}"
                Timeline.CarEventType.DEPARTURE ->
                    "\tCar left the parking through the gate id=${event.gateId}"
            }
        )
    }

    class UnknownGateException(val event: Timeline.CarEvent) : IllegalArgumentException(when (event.type) {
        Timeline.CarEventType.ARRIVAL ->
            "Car is supposed to arrive at the gate with id=${event.gateId} at the time=${event.time} " +
                    "but this gate does not exist."
        Timeline.CarEventType.DEPARTURE ->
            "Car is supposed to leave through the gate with id=${event.gateId} at the time=${event.time} " +
                    "but this gate does not exist."
    })
}
