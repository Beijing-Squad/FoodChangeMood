package org.beijing

import di.dataModule
import di.logicModule
import di.uiModule
import org.beijing.presentation.FoodConsoleUi
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin { modules(dataModule, logicModule, uiModule) }
    val ui = getKoin().get<FoodConsoleUi>()
    ui.runExploreCountryGame()
}
