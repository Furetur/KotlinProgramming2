package homeworks.hw1

object ComputersGenerator {
    private val windows = OperatingSystem("win", "Windows", 100)
    private val linux = OperatingSystem("linux", "Arch Linux (nerds only)", 50)
    private val mac = OperatingSystem("mac", "MacOS", 0)

    fun makeWinPc(id: Int, infected: Boolean = false): LocalNetwork.Computer
            = LocalNetwork.Computer(id, windows, infected)
    fun makeLinuxPc(id: Int, infected: Boolean = false): LocalNetwork.Computer
            = LocalNetwork.Computer(id, linux, infected)
    fun makeMacOsPc(id: Int, infected: Boolean = false): LocalNetwork.Computer
            = LocalNetwork.Computer(id, mac, infected)
}
