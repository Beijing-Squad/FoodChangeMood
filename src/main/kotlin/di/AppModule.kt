package org.beijing.di

import di.dataModule
import di.logicModule
import di.uiModule

val appModule = listOf(dataModule, logicModule, uiModule)