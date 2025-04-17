package logic


import org.beijing.logic.MealRepository
import org.beijing.model.Meal

class SearchMealsByNameUseCase(
    private val mealRepository: MealRepository,
    private val substringSearch: KmpSubstringSearch
) {

    /**
     * Executes the search for meals that contain the query string in their name.
     * Throws exception if query is blank or data is not available.
     */
    fun execute(searchQuery: String): List<Meal> {
        if (searchQuery.isBlank()) {
            throw IllegalArgumentException("Search query must not be blank.")
        }

        val allMeals = mealRepository.getAllMeals()
        if (allMeals.isEmpty()) {
            throw IllegalStateException("No food data available to search.")
        }

        // Filter meals where the name contains the query (case-insensitive)
        return allMeals.filter { meal ->
            substringSearch.doesTextContainPattern(
                meal.name.lowercase(),
                searchQuery.lowercase()
            )
        }
    }
}