package presentation.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import logic.usecases.fakedata.mealsListWithHighCaloriesMeals
import org.beijing.logic.usecases.ManageMealsSuggestionsUseCase
import org.beijing.presentation.ViewMealDetails
import org.beijing.presentation.service.SuggestionMealsService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import presentation.view_read.ConsoleIO

class SuggestionMealsServiceTest {
    private lateinit var useCase: ManageMealsSuggestionsUseCase
    private lateinit var service: SuggestionMealsService
    private lateinit var viewMealDetails: ViewMealDetails
    private lateinit var consoleIO: ConsoleIO

    @BeforeEach
    fun setup() {
        useCase = mockk(relaxed = true)
        viewMealDetails = mockk(relaxed = true)
        consoleIO = mockk(relaxed = true)
        service = SuggestionMealsService(useCase, viewMealDetails, consoleIO)
    }

    //region handle user choice
    @Test
    fun handleUserChoice() {
    }
    //endregion
    //region suggest meal have more than seven hundred calories
    @Test
    fun `should call launchSoThinMeals when suggest meal with more than 700 calories selected`() {
        // Given
        val choiceSuggestionFeature = "6"
        val likeMeal="yes"
        every { consoleIO.readInput() } returns choiceSuggestionFeature andThen likeMeal
        every { useCase.suggestMealHaveMoreThanSevenHundredCalories() } returns  mealsListWithHighCaloriesMeals

        // When
        service.handleUserChoice()

        // Then
        verify {
            consoleIO.viewWithLine("Do You Like This Meal?")
            consoleIO.view("write 'yes' to get details or 'no' to get another meal Or 'exit':")
        }
    }

    //endregion
}