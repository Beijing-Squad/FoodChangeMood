package org.beijing.presentation.service

abstract class MealService {
    open fun showService() {
        showOptionService()
        handleUserChoice()
    }

    abstract fun showOptionService()

    abstract fun handleUserChoice()

    protected fun getUserInput(): String? {
        return readlnOrNull()?.trim()
    }
}