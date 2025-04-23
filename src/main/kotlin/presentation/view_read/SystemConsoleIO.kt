package presentation.view_read

class SystemConsoleIO : ConsoleIO {
    override fun view(message: String) = kotlin.io.print(message)
    override fun viewWithLine(message: String?) = kotlin.io.println(message)
    override fun readInput(): String? = readlnOrNull()
}
