package org.beijing.presentation.service

import org.beijing.logic.usecases.SearchMealsUseCases

fun searchMealService(searchMealsUseCases: SearchMealsUseCases) {

    showOptionsForSearchMealService()
    print("\nhere: \n")
    when (val input = getUserInput()) {
 feature/63-identify-iraqi-meals
        1 -> launchGymHelper(searchMealsUseCases)



        1 -> launchGymHelper()
        2 -> launchSearchByName()
        3 -> launchMealsByDate()
        4 -> searchMealByCountryService()
 develop
        0 -> return

        else -> println("Invalid input: $input")
    }
    searchMealService(searchMealsUseCases)
}

fun showOptionsForSearchMealService() {
    println("\n\n ===Please enter one of the numbers listed below===\n")
    println("1. Gym Helper")
    println("0. Exit")
}

private fun getUserInput(): Int? {
    return readlnOrNull()?.toIntOrNull()
}

// region gym helper
private fun launchGymHelper(searchMealsUseCases: SearchMealsUseCases) {
    print("enter target of Calories: ")
    val targetCalories = readlnOrNull()?.toDoubleOrNull()
    print("enter target of Protein:")
    val targetProtein = readlnOrNull()?.toDoubleOrNull()
    if (targetProtein != null && targetCalories != null) {
        println(
            searchMealsUseCases.getGymHelperMeals(
                targetCalories,
                targetProtein
            )
        )
    }
}
feature/63-identify-iraqi-meals
//endregion

//endregion

// region search meal by country
fun searchMealByCountryService() {
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
                val meals = searchMeals.searchMealByCountry(country)
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
// end region search meal by country
develop
