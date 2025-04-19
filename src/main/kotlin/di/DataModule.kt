package di

import org.beijing.data.CsvFileParser
import org.beijing.data.CsvFileReader
import org.beijing.data.CsvMealRepository
import org.beijing.logic.MealRepository
import org.koin.dsl.module
import java.io.File

val dataModule = module {
    single { File("food.csv") }
    single { CsvFileReader(get()) }
    single { CsvFileParser() }
    single<MealRepository> { CsvMealRepository(get(), get()) }
}