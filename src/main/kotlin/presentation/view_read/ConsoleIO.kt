package presentation.view_read

interface ConsoleIO {
    fun print(message: String)
    fun println(message: String?)
    fun readInput(): String?
}
