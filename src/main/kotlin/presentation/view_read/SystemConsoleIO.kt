package presentation.view_read

class SystemConsoleIO : ConsoleIO {
    override fun view(message: String) = print(message)
    override fun viewWithLine(message: String?) = println(message)
    override fun readInput(): String? = readlnOrNull()
}
