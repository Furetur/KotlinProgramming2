package homeworks.hw1

import java.io.File

const val FILE_PATH = "./src/main/resources/homeworks/hw1/initialState.json"

fun main() {
    val file = File(FILE_PATH)
    val localNetwork = InitialStateParser.getLocalNetworkFromFile(file)
    val gameLoop = GameLoop(localNetwork)
    gameLoop.start()
}
