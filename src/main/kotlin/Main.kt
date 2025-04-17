package org.beijing

import org.beijing.di.appModule
import org.beijing.presentation.FoodConsoleUi
import di.dataModule
import di.logicModule
import di.uiModule
import org.beijing.presentation.FoodConsoleUi
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin { modules(dataModule, logicModule, uiModule) }
    val ui0 = getKoin().get<FoodConsoleUi>()
    ui0.runExploreCountryGame()


    val ui: FoodConsoleUi = getKoin().get()
    ui.start()

}