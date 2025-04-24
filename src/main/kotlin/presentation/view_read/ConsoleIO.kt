package presentation.view_read

interface ConsoleIO {
    fun view(message: String)
    fun viewWithLine(message: String?)
    fun readInput(): String?
}
