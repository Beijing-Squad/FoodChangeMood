package org.beijing

import org.beijing.di.appModule
import org.beijing.presentation.FoodUi
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatform.getKoin

fun main() {
    startKoin { modules(appModule) }
    val ui: FoodUi = getKoin().get()
    ui.start()
}