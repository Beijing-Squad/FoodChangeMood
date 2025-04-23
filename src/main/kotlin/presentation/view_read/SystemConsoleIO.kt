package presentation.view_read

class SystemConsoleIO : ConsoleIO {
    override fun print(message: String) = kotlin.io.print(message)
    override fun println(message: String?) = kotlin.io.println(message)
    override fun readInput(): String? = readlnOrNull()
}
