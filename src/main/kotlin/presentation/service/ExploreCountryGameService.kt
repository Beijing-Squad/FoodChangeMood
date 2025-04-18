package org.beijing.presentation.service

import org.beijing.logic.usecases.ExploreCountryMealsUseCase
import org.koin.mp.KoinPlatform.getKoin

fun exploreCountryGameService() {
    val exploreCountryMealsUseCase: ExploreCountryMealsUseCase = getKoin().get()
    println("🎌 Welcome to 'Explore Other Countries' Food Culture'!")
    println("------------------------------------------------------")
    println("🍱 In this mini-game, you enter a country name and discover up to 20 random meals from that region.")
    println("🌍 For example, try entering 'Italy', 'India', or 'Mexico'.")

    while (true) {
        println("\n🔎 Enter a country name (or type 'exit' to quit):")
        val country = readlnOrNull()?.trim()

        when {
            country.equals("exit", ignoreCase = true) -> {
                println("👋 Thanks for playing! Come back soon!")
                break
            }

            country.isNullOrBlank() || country.length < 4 -> {
                println("⚠️ Please enter a country name with at least 4 characters.")
                continue
            }

            country.all { it.isDigit() } -> {
                println("🚫 Please enter a valid name, not just numbers.")
                continue
            }

            else -> {
                val meals = exploreCountryMealsUseCase.exploreCountryMeals(country)
                if (meals.isEmpty()) {
                    println("😔 Sorry, no meals found for '$country'. Try another country!")
                } else {
                    println("\n🍽️ Found ${meals.size} meal(s) related to '$country':\n")
                    meals.forEachIndexed { index, meal ->
                        println("${index + 1}. ${meal.name} • ⏱️ ${meal.minutes} mins • 🧂 ${meal.nIngredients} ingredients • 🔧 ${meal.nSteps} steps")
                    }
                }
            }
        }
    }
}


