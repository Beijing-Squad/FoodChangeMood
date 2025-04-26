package org.beijing.presentation.service

import presentation.view_read.ConsoleIO

abstract class MealService(
    private val consoleIO: ConsoleIO
) {
    open fun showService() {
        showOptionService()
        handleUserChoice()
    }

    abstract fun showOptionService()

    abstract fun handleUserChoice()

    protected fun getUserInput(): String? {
        return consoleIO.readInput()?.trim()
    }
}