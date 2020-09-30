package homeworks.hw3

object TimelessEventsGenerator {
    fun makeArrivalEvent(): Timeline.CarEvent = Car(0, 0).arrival
}
