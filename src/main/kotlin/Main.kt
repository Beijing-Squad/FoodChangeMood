package org.beijing

import org.beijing.di.appModule
import org.koin.core.context.startKoin

fun main() {

    startKoin { modules(appModule) }

}