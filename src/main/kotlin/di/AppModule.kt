package org.beijing.di

import di.dataModule
import di.logicModule
import di.uiModule
import org.koin.dsl.module


val appModule = module {
    listOf(dataModule, logicModule, uiModule)
}



