package presentation.service

import org.junit.jupiter.api.Test

class SuggestionMealsServiceTest {

    //region handle user choice
    @Test
    fun handleUserChoice() {
    }
    //endregion

    //region ten random potato meals ui test
    @Test
    fun `should call get ten random potato meals when selected`() {
        // Given
        val choiceSearchFeature = "5"
        val exitInput = "0"

        every { consoleIO.readInput() } returns choiceSearchFeature andThen exitInput

        // When
        suggestMealService.handleUserChoice()

        // Then
        verify {
            manageMealsSuggest.suggestTenRandomMealsContainsPotato()
        }
    }

    @Test
    fun `should show the error message when throw an exception`() {
        // Given
        val errorMessage = "no meals contains potato found"

        every { manageMealsSuggest.suggestTenRandomMealsContainsPotato() } throws IllegalArgumentException(errorMessage)

        // When
        suggestMealService.launchTenRandomPotatoMeals()

        // Then
        verify {
            consoleIO.viewWithLine(errorMessage)
        }
    }
    //endregion
}