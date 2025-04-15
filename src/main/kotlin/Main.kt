package org.beijing

import di.uiModule
import org.beijing.di.appModule
import org.beijing.presentation.FoodConsoleUi
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {

    startKoin { modules(appModule) }

    val ui: FoodConsoleUi = getKoin().get()
    ui.start()

}