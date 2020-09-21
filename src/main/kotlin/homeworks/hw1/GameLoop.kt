package homeworks.hw1

class GameLoop(private val localNetwork: LocalNetwork) {

    init {
        if (localNetwork.infectedComputers.isEmpty()) {
            throw NoInfectedComputersException()
        }
    }

    private var currentTurn = 1

    fun start() {
        while (!localNetwork.isEveryComputerInfected) {
            iterate()
        }
    }

    private fun iterate() {
        localNetwork.trySpreadVirus()
        logState()
        currentTurn += 1
    }

    private fun logState() {
        val infected = localNetwork.infectedComputers.joinToString("\n\t")

        println("After turn $currentTurn the infected computers are:\n\t$infected")
    }

    class NoInfectedComputersException : IllegalArgumentException()
}
